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
                .Include(x => x.User)
                .Include(x => x.HouseworkStatus)
                .Include(x => x.Housework)
                .ThenInclude(y => y.HouseworkSettings)
                .ThenInclude(z => z.Frequency)
                .FirstOrDefaultAsync(x => x.Id == scheduleId)
                .ConfigureAwait(false);
                

            if(schedule == null)
            {
                _logger.LogError($"Schedule not found for id {scheduleId}");
                return new BadRequestResult();
            }

            if(schedule.HouseworkStatus == null)
            {
                _logger.LogError($"Status not found for scheduleID {scheduleId}");
                return new BadRequestResult();
            }

            return schedule.ToScheduleFullGetModel();
        }

        [HttpPost("", Name = nameof(CreateNewSchedule))]
        public async Task<ActionResult<SchedulePutReturnModel>> CreateNewSchedule(SchedulePutModel schedule)
        {
            var housework = await _sqlContext.Houseworks.FindAsync(schedule.HouseworkId).ConfigureAwait(false);

            if(housework == null)
            {
                _logger.LogError($"Housework not found for id {schedule.HouseworkId}");
                return new BadRequestResult();
            }

            var scheduleEntity = schedule.ToScheduleModel(housework);

            scheduleEntity.StatusId = 1;

            var status = await _sqlContext.HouseworkStatuses.FindAsync(1).ConfigureAwait(false);

            scheduleEntity.HouseworkStatus = status;

            await _sqlContext.HouseworkSchedules.AddAsync(scheduleEntity).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return scheduleEntity.ToSchedulePutReturnModel();
        }

        [HttpGet("/date", Name = nameof(GetScheduleDate))]
        public async Task<ActionResult<List<ScheduleDateModel>>> GetScheduleDate([FromQuery] FromToDateModel dates)
        {
            List<ScheduleDateModel> schedulesModels = new List<ScheduleDateModel>();

            var schedules = await _sqlContext.HouseworkSchedules
                .Where(x => x.Date >= dates.From && x.Date <= dates.To)
                .Include(y => y.Housework)
                .ToListAsync()
                .ConfigureAwait(false);

            schedulesModels = schedules.Select(x => x.ToScheduleDateModel()).ToList();

            return schedulesModels;
        }

        [HttpGet("{houseworkId}/list", Name = nameof(GetFullSchedulesByDate))]
        public async Task<ActionResult<List<ScheduleFullGetModel>>> GetFullSchedulesByDate([FromQuery] FromToDateModel dates, int houseworkId)
        {
            List<ScheduleFullGetModel> schedulesModels = new List<ScheduleFullGetModel>();

            var schedules = await _sqlContext.HouseworkSchedules
                .Where(x => x.Date >= dates.From && x.Date <= dates.To && x.HouseworkId == houseworkId)
                .Include(x => x.User)
                .Include(x => x.HouseworkStatus)
                .Include(x => x.Housework)
                .ThenInclude(y => y.HouseworkSettings)
                .ThenInclude(z => z.Frequency)
                .ToListAsync()
                .ConfigureAwait(false);

            schedulesModels = schedules.Select(x => x.ToScheduleFullGetModel()).ToList();

            return schedulesModels;
        }


    }
}
