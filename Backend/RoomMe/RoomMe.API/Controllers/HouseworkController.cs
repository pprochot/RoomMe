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

        [HttpPut("", Name = nameof(PutHousework))]
        public async Task<ActionResult<HouseworkPutReturnModel>> PutHousework(HouseworkPutModel housework)
        {

            foreach (var userId in housework.Users)
            {
                var existsUser = await _sqlContext.Users.AnyAsync(x => x.Id == userId).ConfigureAwait(false);

                if (!existsUser)
                {
                    _logger.LogError($"User not found for id {userId}");
                    return new BadRequestResult();
                }
            }

            var existsAuthor = await _sqlContext.Users.AnyAsync(x => x.Id == housework.AuthorId).ConfigureAwait(false);

            if (!existsAuthor)
            {
                _logger.LogError($"User not found for id {housework.AuthorId}");
                return new BadRequestResult();
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

            var houseworkEntity = housework.ToHouseworkModel();
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

            return houseworkEntity.ToHouseworkPutReturnModel(settings.Id);
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

        [HttpGet("list", Name = nameof(GetFullHouseworkByDate))]
        public async Task<ActionResult<List<HouseworkFullGetModel>>> GetFullHouseworkByDate(FromToDateModel dates)
        {
            List<HouseworkFullGetModel> houseworkModels = new List<HouseworkFullGetModel>();

            var houseworks = await _sqlContext.HouseworkSchedules
                .Where(x => x.Date >= dates.From && x.Date <= dates.To)
                .Include(y => y.Housework)
                .Select(x => x.Housework)
                .ToListAsync()
                .ConfigureAwait(false);

            houseworkModels = houseworks.Select(x => x.ToHouseworkFullModel()).ToList();

            return houseworkModels;
        }
    }
}
