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
    public class ScheduleController
    {
        private readonly ILogger<ScheduleController> _logger;
        private readonly SqlContext _sqlContext;
        public ScheduleController(ILogger<ScheduleController> logger, SqlContext sqlContext)
        {
            _logger = logger;
            _sqlContext = sqlContext;
        }

        [HttpGet("{scheduleId}/full", Name = nameof(GetScheduleFull))]
        public async Task<ActionResult<ScheduleFullGetModel>> GetScheduleFull(int scheduleId)
        {
            var schedule = await _sqlContext.HouseworkSchedules
                .Include(x => x.StatusId)
                .Include(y => y.HouseworkSettings.Id)
                .FirstOrDefaultAsync(x => x.Id == scheduleId)
                .ConfigureAwait(false);

            if(schedule == null)
            {
                _logger.LogError($"Schedule not found for id {scheduleId}");
                return new BadRequestResult();
            }

            return schedule.ToScheduleFullGetModel();
        }

        [HttpPut("", Name = nameof(CreateNewSchedule))]
        public async Task<ActionResult<SchedulePutReturnModel>> CreateNewSchedule(SchedulePutModel schedule)
        {
            var housework = await _sqlContext.Houseworks.FindAsync(schedule.HouseworkId).ConfigureAwait(false);

            if(housework == null)
            {
                _logger.LogError($"Housework not found for id {schedule.HouseworkId}");
                return new BadRequestResult();
            }

            var user = await _sqlContext.Users.FindAsync(schedule.UserId).ConfigureAwait(false);

            if(user == null)
            {
                _logger.LogError($"User not found for id {schedule.UserId}");
                return new BadRequestResult();
            }

            var status = await _sqlContext.HouseworkStatuses.FindAsync(schedule.StatusId).ConfigureAwait(false);

            if(status == null)
            {
                _logger.LogError($"Status not found for id {schedule.StatusId}");
                return new BadRequestResult();
            }

            var settings = await _sqlContext.HouseworkSettings.FindAsync(schedule.SettingsId).ConfigureAwait(false);

            if (settings == null)
            {
                _logger.LogError($"Settings not found for id {schedule.StatusId}");
                return new BadRequestResult();
            }

            var scheduleEntity = schedule.ToScheduleModel(housework,user, status, settings);
            await _sqlContext.HouseworkSchedules.AddAsync(scheduleEntity).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return scheduleEntity.ToSchedulePutReturnModel();
        }
    }
}
