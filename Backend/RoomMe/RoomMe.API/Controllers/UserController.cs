using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using RoomMe.API.Authorization;
using RoomMe.API.Converters;
using RoomMe.API.Helpers;
using RoomMe.API.Models;
using RoomMe.API.Validators;
using RoomMe.SQLContext;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Controllers
{
    [JWTAuthorize]
    [ApiController]
    [Route("[controller]")]
    public class UserController : ControllerBase
    {
        private readonly ILogger<UserController> _logger;
        private readonly SqlContext _sqlContext;
        private readonly ISessionHelper _sessionHelper;

        public UserController(ILogger<UserController> logger, SqlContext sqlContext, ISessionHelper sessionHelper)
        {
            _logger = logger;
            _sqlContext = sqlContext;
            _sessionHelper = sessionHelper;
        }

        [HttpGet("list", Name = nameof(GetUsers))]
        public async Task<IEnumerable<UserShortModel>> GetUsers(string phrase)
        {
            var users = await _sqlContext.Users
                .Where(x => x.Nickname.StartsWith(phrase) || x.Firstname.StartsWith(phrase) || x.Lastname.StartsWith(phrase))
                .Where(x => x.Id != _sessionHelper.UserId)
                .Where(x => !_sessionHelper.FriendsIds.Any(id => id == x.Id))
                .ToListAsync()
                .ConfigureAwait(false);

            return users.ToUserShortListModel();
        }

        [HttpGet("", Name = nameof(GetUserInfo))]
        public async Task<ActionResult<UserGetModel>> GetUserInfo()
        {
            var userId = _sessionHelper.UserId;
            var user = await _sqlContext.Users
                .FindAsync(userId);

            return user.ToUserGetModel();
        }

        [HttpGet("flats", Name = nameof(GetFlats))]
        public async Task<ActionResult<IEnumerable<FlatNameModel>>> GetFlats()
        {
            var userId = _sessionHelper.UserId;
            var user = await _sqlContext.Users
                .Include(x => x.Flats)
                .FirstOrDefaultAsync(x => x.Id == userId)
                .ConfigureAwait(false);

            return user.Flats.ToFlatNameModelList();
        }

        [HttpPost("friends/{friendId}", Name = nameof(AddFriend))]
        public async Task<ActionResult<DateTime>> AddFriend(int friendId)
        {
            var userId = _sessionHelper.UserId;

            var user = await _sqlContext.Users
                .Include(x => x.Friends)
                .SingleOrDefaultAsync(x => x.Id == userId)
                .ConfigureAwait(false);

            var friend = await _sqlContext.Users
                .FindAsync(friendId)
                .ConfigureAwait(false);

            if (friend == null)
            {
                _logger.LogError($"User not found for id {friendId}");
                return new BadRequestResult();
            }

            if (user.Friends.Any(x => x.FriendId == friendId))
            {
                _logger.LogError($"User has already friend for given id: {friendId}");
                return new BadRequestResult();
            }

            await _sqlContext.UserFriends.AddAsync(new UserFriend() { UserId = userId, FriendId = friendId }).ConfigureAwait(false);
            await _sqlContext.UserFriends.AddAsync(new UserFriend() { UserId = friendId, FriendId = userId }).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return DateTime.Now;
        }

        [HttpDelete("friends/{friendId}", Name = nameof(DeleteFriend))]
        public async Task<ActionResult> DeleteFriend(int friendId)
        {
            var userId = _sessionHelper.UserId;
            var user = await _sqlContext.Users
                .Include(x => x.Friends)
                .FirstOrDefaultAsync(x => x.Id == userId)
                .ConfigureAwait(false);

            if(!user.Friends.Any(x => x.FriendId == friendId))
            {
                _logger.LogError($"Not found friend for given id: {friendId}");
                return new BadRequestResult();
            }

            var entity = user.Friends.Single(x => x.FriendId == friendId);

            _sqlContext.UserFriends.Remove(entity);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return Ok();
        }

        [HttpGet("friends", Name = nameof(GetFriends))]
        public async Task<ActionResult<IEnumerable<UserShortModel>>> GetFriends()
        {
            var userId = _sessionHelper.UserId;
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

            return user.Friends.ToUserShortListModel();
        }
    }
}
