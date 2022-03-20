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

namespace RoomMe.API.Controllers
{
    [Route("[controller]")]
    [ApiController]
    [JWTAuthorize]
    public class HouseworkController : ControllerBase
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

            if (housework == null)
            {
                _logger.LogError($"Housework not found for id {houseworkId}");
                return new BadRequestResult();
            }

            return housework.ToHouseworkFullModel();
        }

        [HttpPut("", Name = nameof(PutHousework))]
        public async Task<ActionResult<HouseworkPutReturnModel>> PutHousework(HouseworkPutModel housework)
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

            var existsFlat = await _sqlContext.Flats.AnyAsync(x => x.Id == housework.FlatId).ConfigureAwait(false);

            if (!existsFlat)
            {
                _logger.LogError($"Flat not found for id {housework.FlatId}");
                return new BadRequestResult();
            }

            var existsFrequency = await _sqlContext.HouseworkFrequencies.AnyAsync(x => x.Id == housework.FrequencyId).ConfigureAwait(false);

            if (!existsFrequency)
            {
                _logger.LogError($"Frequency not found for id {housework.FrequencyId}");
                return new BadRequestResult();
            }

            var houseworkEntity = await _sqlContext.Houseworks
                .Include(x => x.HouseworkSettings)
                .FirstOrDefaultAsync(x => x.Id == housework.Id)
                .ConfigureAwait(false);

            int settingsId = -1;

            if (houseworkEntity == null)
            {
                houseworkEntity = housework.ToHouseworkModel();
                await _sqlContext.Houseworks.AddAsync(houseworkEntity).ConfigureAwait(false);
                await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

                HouseworkSettings settings = new()
                {
                    HouseworkId = houseworkEntity.Id,
                    FrequencyId = housework.FrequencyId,
                    Day = housework.Day
                };

                await _sqlContext.HouseworkSettings.AddAsync(settings).ConfigureAwait(false);
                await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

                settingsId = settings.Id;
            }
            else
            {
                houseworkEntity.UpdateHousework(housework, users);
                _sqlContext.Houseworks.Update(houseworkEntity);
                await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

                settingsId = houseworkEntity.HouseworkSettings.Id;
            }

            return houseworkEntity.ToHouseworkPutReturnModel(settingsId);
        }

        [HttpGet("{houseworkId}/settings", Name = nameof(GetHouseworkSettings))]
        public async Task<ActionResult<HouseworkSettingsModel>> GetHouseworkSettings(int houseworkId)
        {
            var housework = await _sqlContext.Houseworks
               .Include(x => x.HouseworkSettings)
               .ThenInclude(y => y.Frequency)
               .FirstOrDefaultAsync(x => x.Id == houseworkId)
               .ConfigureAwait(false);

            if (housework == null)
            {
                _logger.LogError($"Housework not found for id {houseworkId}");
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

    }
}
