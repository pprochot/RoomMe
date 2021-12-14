using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using RoomMe.API.Converters;
using RoomMe.API.Models;
using RoomMe.SQLContext;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class UserController
    {
        private readonly ILogger<UserController> _logger;
        private readonly SqlContext _sqlContext;

        public UserController(ILogger<UserController> logger, SqlContext sqlContext)
        {
            _logger = logger;
            _sqlContext = sqlContext;
        }

        [HttpPost("test/new", Name = nameof(CreateTestUser))]
        public async Task<ActionResult<UserPostReturnModel>> CreateTestUser(UserPostModel user)
        {
            var entity = await _sqlContext.Users
                .FirstOrDefaultAsync(x => x.Email == user.Email)
                .ConfigureAwait(false);

            if (entity != null)
            {
                return new UserPostReturnModel()
                {
                    Result = false,
                    ErrorCode = Helpers.ErrorCodes.UserPost.EmailAlreadyInDB,
                    UserId = null
                };
            }

            entity = user.ToUser();
            await _sqlContext.Users.AddAsync(entity).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return new UserPostReturnModel()
            {
                Result = true,
                ErrorCode = null,
                UserId = entity.Id
            };
        }
    }
}
