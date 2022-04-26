using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using RoomMe.API.Authorization;
using RoomMe.API.Converters;
using RoomMe.API.Helpers;
using RoomMe.API.Models;
using RoomMe.SQLContext;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Controllers
{
    [JWTAuthorize]
    [ApiController]
    [Route("[controller]")]
    public class FlatController : ControllerBase
    {
        private readonly ILogger<FlatController> _logger;
        private readonly SqlContext _sqlContext;
        private readonly ISessionHelper _sessionHelper;

        public FlatController(ILogger<FlatController> logger, SqlContext sqlContext, ISessionHelper sessionHelper)
        {
            _logger = logger;
            _sqlContext = sqlContext;
            _sessionHelper = sessionHelper;
        }

        [HttpGet("{flatId}", Name = nameof(GetFlat))]
        public async Task<ActionResult<FlatGetModel>> GetFlat(int flatId)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.Users)
                .FirstOrDefaultAsync(x => x.Id == flatId)
                .ConfigureAwait(false);

            if (flat == null || !_sessionHelper.IsUserOfFlat(flat))
            {
                _logger.LogError($"Flat not found for id {flatId}");
                return new BadRequestResult();
            }

            return flat.ToFlatGetModel();
        }

        [HttpGet("{flatId}/users", Name = nameof(GetFlatUsers))]
        public async Task<ActionResult<FlatUsersGetReturnModel>> GetFlatUsers(int flatId)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.Users)
                .Include(x => x.Creator)
                .FirstOrDefaultAsync(x => x.Id == flatId)
                .ConfigureAwait(false);

            if(flat == null || !_sessionHelper.IsUserOfFlat(flat))
            {
                _logger.LogError($"Flat not found for id {flatId}");
                return new BadRequestResult();
            }

            return flat.ToFlatUsersGetReturnModel();
        }

        [HttpPost("", Name = nameof(CreateNewFlat))]
        public async Task<ActionResult<FlatPostReturnModel>> CreateNewFlat(FlatPostModel flat) 
        {
            List<User> users = new();

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

            users.Add(_sessionHelper.Session);

            var flatEntity = flat.ToFlatModel(users, _sessionHelper.Session);
            await _sqlContext.Flats.AddAsync(flatEntity).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return flatEntity.ToFlatPostReturnModel();
        }

        [HttpPost("{flatId}/user/{userId}", Name = nameof(AddUserToFlat))]
        public async Task<ActionResult<UserShortModel>> AddUserToFlat(int flatId, int userId)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.Users)
                .SingleOrDefaultAsync(x => x.Id == flatId)
                .ConfigureAwait(false);

            if(flat == null || !_sessionHelper.IsCreatorOfFlat(flat) || flat.Users.Any(x => x.Id == userId))
            {
                return new BadRequestResult();
            }

            var user = await _sqlContext.Users.FindAsync(userId).ConfigureAwait(false);

            if(user == null)
            {
                return new BadRequestResult();
            }

            flat.Users.Add(user);

            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return user.ToUserShortModel();
        }

        [HttpDelete("{flatId}/user/{userId}", Name = nameof(RemoveUserFromFlat))]
        public async Task<ActionResult> RemoveUserFromFlat(int flatId, int userId)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.Users)
                .SingleOrDefaultAsync(x => x.Id == flatId)
                .ConfigureAwait(false);

            if(flat == null || !_sessionHelper.IsCreatorOfFlat(flat))
            {
                return new BadRequestResult();
            }

            var user = await _sqlContext.Users.FindAsync(userId).ConfigureAwait(false);

            if(user == null)
            {
                return new BadRequestResult();
            }

            flat.Users.Remove(user);

            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return Ok();
        }

        [HttpPut("{flatId}/rent", Name = nameof(SetFlatRentCost))]
        public async Task<ActionResult<RentCostPostReturnModel>> SetFlatRentCost(int flatId, RentCostPutModel cost)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.RentCosts)
                .Include(x => x.Users)
                .FirstOrDefaultAsync(x => x.Id == flatId)
                .ConfigureAwait(false);

            if (flat == null)
            {
                _logger.LogError($"Not found flat for given id {flatId}");
                return new BadRequestResult();
            }

            var userId = _sessionHelper.UserId;
            var user = flat.Users.FirstOrDefault(x => x.Id == userId);

            if (user == null)
            {
                _logger.LogError($"Not found user for given id {userId}");
                return new BadRequestResult();
            }

            var rentId = flat.RentCosts.FindIndex(x => x.UserId == userId);
            RentCost entity = null;

            if (rentId == -1)
            {
                entity = cost.ToRentCost(flatId, userId);
                flat.RentCosts.Add(entity);
            }
            else
            {
                flat.RentCosts[rentId].UpdateRentCost(cost);
                entity = flat.RentCosts[rentId];
            }

            _sqlContext.Update(flat);
            await _sqlContext.SaveChangesAsync();

            return entity.ToRentCostPostReturnModel();
        }

        [HttpGet("{flatId}/shopping-lists", Name = nameof(GetFlatShoppingLists))]
        public async Task<ActionResult<IEnumerable<ShoppingListShortModel>>> GetFlatShoppingLists(int flatId)
        {
            var lists = await _sqlContext.ShoppingLists
                .Include(x => x.Flat)
                .ThenInclude(y => y.Users)
                .Where(x => x.FlatId == flatId)
                .ToListAsync()
                .ConfigureAwait(false);

            if (lists.Any() && !_sessionHelper.IsUserOfFlat(lists.First().Flat))
            {
                return new BadRequestResult();
            }

            return lists.Select(x => x.ToShoppingListShortModel()).ToList();
        }

        [HttpGet("{flatId}/available-locators", Name = nameof(GetAvailableLocators))]
        public async Task<ActionResult<IEnumerable<UserNicknameModel>>> GetAvailableLocators(int flatId)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.Users)
                .SingleOrDefaultAsync(x => x.Id == flatId)
                .ConfigureAwait(false);

            if(flat == null || _sessionHelper.IsCreatorOfFlat(flat))
            {
                return new BadRequestResult();
            }

            var friends = await _sqlContext.UserFriends
                .Include(x => x.Friend)
                .Where(x => x.UserId == _sessionHelper.UserId)
                .ToListAsync()
                .ConfigureAwait(false);

            var res = new List<UserNicknameModel>();

            foreach(var friend in friends)
            {
                if(!flat.Users.Contains(friend.Friend))
                {
                    res.Add(friend.Friend.ToUserNicknameModel());
                }
            }

            return res;
        }
    }
}
