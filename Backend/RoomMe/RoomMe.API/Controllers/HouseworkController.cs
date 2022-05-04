using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Microsoft.AspNetCore.Authorization;
using RoomMe.API.Converters;
using RoomMe.API.Models;
using RoomMe.SQLContext;
using RoomMe.SQLContext.Models;
using RoomMe.API.Authorization;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using RoomMe.API.Helpers;
using RoomMe.API.Extensions;

namespace RoomMe.API.Controllers
{
    [Route("[controller]")]
    [ApiController]
    [JWTAuthorize]
    public class HouseworkController : ControllerBase
    {
        private readonly ILogger<HouseworkController> _logger;
        private readonly SqlContext _sqlContext;
        private readonly ISessionHelper _sessionHelper;
        public HouseworkController(ILogger<HouseworkController> logger, SqlContext sqlContext, ISessionHelper sessionHelper)
        {
            _logger = logger;
            _sqlContext = sqlContext;
            _sessionHelper = sessionHelper;
        }

        [HttpGet("{houseworkId}", Name = nameof(GetHouseworkFull))]
        public async Task<ActionResult<HouseworkModel>> GetHouseworkFull(int houseworkId)
        {
            var housework = await _sqlContext.Houseworks
                            .Include(x => x.Author)
                            .Include(y => y.Flat)
                            .ThenInclude(z => z.Users)
                            .Include(z => z.Users)
                            .Include(x => x.HouseworkSettings)
                            .ThenInclude(x => x.Frequency)
                            .Include(p => p.HouseworkSchedules)
                            .FirstOrDefaultAsync(x => x.Id == houseworkId)
                            .ConfigureAwait(false);


            if (housework == null)
            {
                _logger.LogError($"Housework not found for id {houseworkId}");
                return new BadRequestResult();
            }
            
            if (!_sessionHelper.IsUserOfFlat(housework.Flat))
            {
                _logger.LogError($"User is not in flat for housework id {houseworkId}");
                return new BadRequestResult();
            }

            housework.GenerateSchedules(DateTime.UtcNow.AddDays(31), _sqlContext);

            _sqlContext.SaveChanges();

            return housework.ToHouseworkModel();
        }

        [HttpPost("", Name = nameof(PostHousework))]
        public async Task<ActionResult<HouseworkPostReturnModel>> PostHousework(HouseworkPostModel housework)
        {
            var users = await _sqlContext.Users
                .Where(x => housework.Users.Contains(x.Id))
                .ToListAsync()
                .ConfigureAwait(false);

            foreach (var userId in housework.Users)
            {
                var existsUser = users.Any(x => x.Id == userId);

                if (!existsUser)
                {
                    _logger.LogError($"User not found for id {userId}");
                    return new BadRequestResult();
                }
            }

            var flat = await _sqlContext.Flats.Include(x => x.Users).FirstOrDefaultAsync(x => x.Id == housework.FlatId).ConfigureAwait(false);

            if (flat == null)
            {
                _logger.LogError($"Flat not found for id {housework.FlatId}");
                return new BadRequestResult();
            }

            if (!_sessionHelper.IsUserOfFlat(flat))
            {
                return new BadRequestResult();
            }

            var existsFrequency = await _sqlContext.HouseworkFrequencies.AnyAsync(x => x.Id == housework.FrequencyId).ConfigureAwait(false);

            if (!existsFrequency)
            {
                _logger.LogError($"Frequency not found for id {housework.FrequencyId}");
                return new BadRequestResult();
            }

            if (housework.Days.Any(x => x <= 0 || x >= 8))
            {
                return new BadRequestResult();
            }

            switch (housework.FrequencyId)
            {
                case (int)Consts.HouseworkFrequencies.Once:
                    if (housework.Days.Length != 1)
                    {
                        return new BadRequestResult();
                    }
                    break;
                case (int)Consts.HouseworkFrequencies.Daily:
                    if (housework.Days.Length != 7)
                    {
                        return new BadRequestResult();
                    }
                    break;
                case (int)Consts.HouseworkFrequencies.TwiceAWeek:
                    if (housework.Days.Length != 2)
                    {
                        return new BadRequestResult();
                    }
                    break;
                case (int)Consts.HouseworkFrequencies.Weekly:
                    if (housework.Days.Length != 1)
                    {
                        return new BadRequestResult();
                    }
                    break;
            }

            var houseworkEntity = housework.ToHouseworkModel(_sessionHelper.UserId, users);
            _sqlContext.Houseworks.Add(houseworkEntity);
            _sqlContext.SaveChanges();

            HouseworkSettings settings = new()
            {
                HouseworkId = houseworkEntity.Id,
                FrequencyId = housework.FrequencyId,
                Days = string.Join(",", housework.Days)
            };

            _sqlContext.HouseworkSettings.Add(settings);
            _sqlContext.SaveChanges();

            houseworkEntity.HouseworkSettings = settings;
            houseworkEntity.GenerateSchedules(DateTime.UtcNow.AddDays(31), _sqlContext);

            _sqlContext.SaveChanges();

            return houseworkEntity.ToHouseworkPutReturnModel(settings.Id);
        }

        [HttpGet("{houseworkId}/settings", Name = nameof(GetHouseworkSettings))]
        public async Task<ActionResult<HouseworkSettingsModel>> GetHouseworkSettings(int houseworkId)
        {
            var housework = await _sqlContext.Houseworks
               .Include(x => x.HouseworkSettings)
               .ThenInclude(y => y.Frequency)
               .Include(x => x.Flat)
               .ThenInclude(y => y.Users)
               .FirstOrDefaultAsync(x => x.Id == houseworkId)
               .ConfigureAwait(false);

            if (housework == null)
            {
                _logger.LogError($"Housework not found for id {houseworkId}");
                return new BadRequestResult();
            }

            if (!_sessionHelper.IsUserOfFlat(housework.Flat))
            {
                _logger.LogError($"User is not in flat for housework id {houseworkId}");
                return new BadRequestResult();
            }

            if (housework.HouseworkSettings == null)
            {
                _logger.LogError($"Settings not found for housework id {houseworkId}");
                return new BadRequestResult();
            }

            if (housework.HouseworkSettings.Frequency == null)
            {
                _logger.LogError($"Frequency not found for settings in housework with Id {houseworkId}");
                return new BadRequestResult();
            }

            return housework.HouseworkSettings.ToHouseworkSettingsModel();
        }

        [HttpDelete("{houseworkId}", Name = nameof(RemoveHousework))]
        public async Task<ActionResult> RemoveHousework(int houseworkId)
        {
            var housework = await _sqlContext.Houseworks
                .Include(x => x.Flat)
                .ThenInclude(x => x.Users)
                .Include(x => x.HouseworkSchedules)
                .Include(x => x.HouseworkSettings)
                .FirstOrDefaultAsync(x => x.Id == houseworkId)
                .ConfigureAwait(false);

            if(housework == null || !_sessionHelper.IsCreatorOfFlat(housework.Flat))
            {
                return new BadRequestResult();
            }

            _sqlContext.HouseworkSettings.Remove(housework.HouseworkSettings);
            _sqlContext.HouseworkSchedules.RemoveRange(housework.HouseworkSchedules);
            _sqlContext.Houseworks.Remove(housework);

            _sqlContext.SaveChanges();

            return Ok();
        }
    }
}
