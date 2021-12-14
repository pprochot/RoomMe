using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using RoomMe.API.Converters;
using RoomMe.API.Models;
using RoomMe.SQLContext;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class FlatController
    {
        private readonly ILogger<FlatController> _logger;
        private readonly SqlContext _sqlContext;
        public FlatController(ILogger<FlatController> logger, SqlContext sqlContext)
        {
            _logger = logger;
            _sqlContext = sqlContext;
        }

        [HttpGet("{flatId}/full", Name = nameof(GetFlatFull))]
        public async Task<ActionResult<FlatFullGetModel>> GetFlatFull(int flatId)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.Users)
                .FirstOrDefaultAsync(x => x.Id == flatId)
                .ConfigureAwait(false);

            if (flat == null)
            {
                _logger.LogError($"Flat not found for id {flatId}");
                return new BadRequestResult();
            }

            return flat.ToFlatFullGetModel();
        }

        [HttpPost("", Name = nameof(CreateNewFlat))]
        public async Task<ActionResult<FlatPostReturnModel>> CreateNewFlat(FlatPostModel flat) 
        {
            List<User> users = new();

            if (flat.Users.Count == 0)
            {
                _logger.LogError("Tried to create flat without users");
                return new BadRequestResult();
            }

            foreach(var userId in flat.Users)
            {
                var entity = await _sqlContext.Users.FindAsync(userId).ConfigureAwait(false);
                
                if (entity == null)
                {
                    _logger.LogError($"User not found for id {userId}");
                    return new BadRequestResult();
                }

                users.Add(entity);
            }

            var flatEntity = flat.ToFlatModel(users);
            await _sqlContext.Flats.AddAsync(flatEntity).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return flatEntity.ToFlatPostReturnModel();
        }
    }
}
