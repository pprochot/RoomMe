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

        [HttpPost("friends/{friendId}", Name = nameof(AddFriend))]
        public async Task<ActionResult<DateTime>> AddFriend(int friendId)
        {
            var userId = _sessionHelper.UserId;

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
        public async Task<ActionResult<IEnumerable<UserShortModel>>> GetFriends(int userId)
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

            return user.Friends.ToUserShortListModel();
        }
    }
}
