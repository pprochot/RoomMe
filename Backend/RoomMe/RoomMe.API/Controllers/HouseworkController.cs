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

            if (housework == null)
            {
                _logger.LogError($"Housework not found for id {houseworkId}");
                return new BadRequestResult();
            }

            return housework.ToHouseworkFullModel();
        }

        [HttpPut("", Name = nameof(CreateNewHousework))]
        public async Task<ActionResult<HouseworkPutReturnModel>> CreateNewHousework(HouseworkPutModel housework, HouseworkSettingsPutModel settings,
            int frequencyId)
        {

            foreach (var user in housework.Users)
            {
                var isUser = await _sqlContext.Users.AnyAsync(x => x.Id == user.Id).ConfigureAwait(false);

                if (isUser == false)
                {
                    _logger.LogError($"User not found for id {user.Id}");
                    return new BadRequestResult();
                }
            }

            var isAuthor = await _sqlContext.Users.AnyAsync(x => x.Id == housework.AuthorId).ConfigureAwait(false);

            if (isAuthor == false)
            {
                _logger.LogError($"User not found for id {housework.AuthorId}");
                return new BadRequestResult();
            }

            var isFlat = await _sqlContext.Flats.AnyAsync(x => x.Id == housework.FlatId).ConfigureAwait(false);

            if (isFlat == false)
            {
                _logger.LogError($"Flat not found for id {housework.FlatId}");
                return new BadRequestResult();
            }

            var houseworkEntity = housework.ToHouseworkModel();
            await _sqlContext.Houseworks.AddAsync(houseworkEntity).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            settings.HouseworkId = houseworkEntity.Id;

            var isFrequency = await _sqlContext.HouseworkFrequencies.AnyAsync(x => x.Id == frequencyId).ConfigureAwait(false);

            if(isFrequency == false)
            {
                _logger.LogError($"Frequency not found for id {frequencyId}");
                return new BadRequestResult();
            }

            settings.FrequencyId = frequencyId;

            var settingsEntity = settings.ToHouseworkSettings();
            await _sqlContext.HouseworkSettings.AddAsync(settingsEntity).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return houseworkEntity.ToHouseworkPutReturnModel(settingsEntity.Id);
        }

        [HttpGet("{houseworkId}/settings", Name = nameof(GetHouseworkSettings))]
        public async Task<ActionResult<HouseworkSettingsModel>> GetHouseworkSettings(int settingsId)
        {
            var settings = await _sqlContext.HouseworkSettings.FindAsync(settingsId).ConfigureAwait(false);

            if(settings == null)
            {
                _logger.LogError($"Settings not found for id {settingsId}");
                return new BadRequestResult();
            }

            return settings.ToHouseworkSettingsModel();
        }

        [HttpGet("{houseworkId}/{settingsId}/frequency", Name = nameof(GetSettingsFrequency))]
        public async Task<ActionResult<HouseworkFrequencyModel>> GetSettingsFrequency(int settingsId)
        {
            var settings = await _sqlContext.HouseworkSettings
                .Include(x => x.Frequency)
                .FirstOrDefaultAsync(y => y.Id == settingsId)
                .ConfigureAwait(false);

            if(settings == null)
            {
                _logger.LogError($"Housework not found for id {settingsId}");
                return new BadRequestResult();
            }

            return settings.Frequency.ToHouseworkFrequencyModel();
        }

        //Houseworks from the given period of time

    }
}
