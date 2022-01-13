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
    [Route("[controller]")]
    [ApiController]
    public class HouseworkController
    {
        private readonly ILogger<HouseworkController> _logger;
        private readonly SqlContext _sqlContext;
        public HouseworkController(ILogger<HouseworkController> logger, SqlContext sqlContext)
        {
            _logger = logger;
            _sqlContext = sqlContext;
        }

        [HttpGet("{houseworkId}/full", Name = nameof(GetHouseworkFull))]
        public async Task<ActionResult<HouseworkFullGetModel>> GetHouseworkFull(int houseworkId)
        {
            var housework = await _sqlContext.Houseworks
                            .Include(x => x.Author)
                            .Include(y => y.Flat)
                            .Include(z => z.Users)
                            .Include(p => p.HouseworkSchedules)
                            .FirstOrDefaultAsync(x => x.Id == houseworkId)
                            .ConfigureAwait(false);

            if(housework == null)
            {
                _logger.LogError($"Housework not found for id {houseworkId}");
                return new BadRequestResult();
            }

            return housework.ToHouseworkFullModel();
        }

        [HttpPut("", Name = nameof(CreateNewHousework))]
        public async Task<ActionResult<HouseworkPutReturnModel>> CreateNewHousework(HouseworkPutModel housework)
        {
            List<User> users = new();

            /*if(housework.Users.Count == 0)
            {
                _logger.LogError($"Tried to create housework without users");
                return new BadRequestResult();
            }*/

            foreach(var userId in housework.Users)
            {
                var entity = await _sqlContext.Users.FindAsync(userId).ConfigureAwait(false);

                if(entity == null)
                {
                    _logger.LogError($"User not found for id {userId}");
                    return new BadRequestResult();
                }

                users.Add(entity);
            }

            var author = await _sqlContext.Users.FindAsync(housework.AuthorId).ConfigureAwait(false);

            if(author == null)
            {
                _logger.LogError($"User not found for id {housework.AuthorId}");
                return new BadRequestResult();
            }

            var flat = await _sqlContext.Flats.FindAsync(housework.FlatId).ConfigureAwait(false);

            if(flat == null)
            {
                _logger.LogError($"Flat not found for id {housework.FlatId}");
                return new BadRequestResult();
            }

            author.Houseworks.Add(housework.ToHouseworkModel(author, flat, users));
            flat.Houseworks.Add(housework.ToHouseworkModel(author, flat, users));

            _sqlContext.Update(author);
            _sqlContext.Update(flat);

            foreach(var entity in users)
            {
                entity.Houseworks.Add(housework.ToHouseworkModel(author, flat, users));
                _sqlContext.Update(entity);
            }

            var houseworkEntity = housework.ToHouseworkModel(author, flat, users);
            await _sqlContext.Houseworks.AddAsync(houseworkEntity).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return houseworkEntity.ToHouseworkPutReturnModel();
        }

        [HttpGet("{houseworkId}/name", Name = nameof(GetHouseworkName))]
        public async Task<ActionResult<HouseworkNameModel>> GetHouseworkName(int houseworkId)
        {
            var housework = await _sqlContext.Houseworks
                .FirstOrDefaultAsync(x => x.Id == houseworkId)
                .ConfigureAwait(false);
            if(housework == null)
            {
                _logger.LogError($"Housework not found for id {houseworkId}");
                return new BadRequestResult();
            }

            return housework.ToHouseworkNameModel();
        }
    }
}
