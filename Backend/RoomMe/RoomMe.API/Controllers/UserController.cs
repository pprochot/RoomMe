using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using RoomMe.API.Authorization;
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
    [Authorize]
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

        [HttpPost("test", Name = nameof(CreateTestUser))]
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

        [HttpGet("{userId}", Name = nameof(GetUserInfo))]
        public async Task<ActionResult<UserGetModel>> GetUserInfo(int userId)
        {
            var user = await _sqlContext.Users
                .FindAsync(userId);

            if(user == null)
            {
                _logger.LogError($"User not found for id {userId}");
                return new BadRequestResult();
            }

            return user.ToUserGetModel();
        }

        [HttpGet("{userId}/flats", Name = nameof(GetFlats))]
        public async Task<ActionResult<IEnumerable<FlatNameModel>>> GetFlats(int userId)
        {
            var user = await _sqlContext.Users
                .Include(x => x.Flats)
                .FirstOrDefaultAsync(x => x.Id == userId)
                .ConfigureAwait(false);

            if(user == null)
            {
                _logger.LogError($"User not found for id {userId}");
                return new BadRequestResult();
            }

            return user.Flats.ToFlatNameModelList();
        }

        [HttpPost("{userId}/friend/{friendId}", Name = nameof(AddFriend))]
        public async Task<ActionResult<DateTime>> AddFriend(int userId, int friendId)
        {
            var user = await _sqlContext.Users
                .FindAsync(userId)
                .ConfigureAwait(false);

            if (user == null)
            {
                _logger.LogError($"User not found for id {userId}");
                return new BadRequestResult();
            }

            var friend = await _sqlContext.Users
                .FindAsync(friendId)
                .ConfigureAwait(false);

            if (friend == null)
            {
                _logger.LogError($"User not found for id {friendId}");
                return new BadRequestResult();
            }

            await _sqlContext.UserFriends.AddAsync(new UserFriend() { UserId = userId, FriendId = friendId }).ConfigureAwait(false);
            await _sqlContext.UserFriends.AddAsync(new UserFriend() { UserId = friendId, FriendId = userId }).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return DateTime.Now;
        }

        [HttpGet("{userId}/friends", Name = nameof(GetFriends))]
        public async Task<ActionResult<IEnumerable<UserFriendModel>>> GetFriends(int userId)
        {
            var user = await _sqlContext.Users
                .Include(x => x.Friends)
                .ThenInclude(y => y.Friend)
                .FirstOrDefaultAsync(x => x.Id == userId)
                .ConfigureAwait(false);

            if(user == null)
            {
                _logger.LogError($"User not found for id {userId}");
                return new BadRequestResult();
            }

            return user.Friends.ToUserFriendListModel();
        }
    }
}
