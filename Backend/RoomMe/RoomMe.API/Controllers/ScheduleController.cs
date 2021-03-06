using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Microsoft.AspNetCore.Authorization;
using RoomMe.API.Converters;
using RoomMe.API.Models;
using RoomMe.SQLContext;
using RoomMe.API.Authorization;
using RoomMe.SQLContext.Models;
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
    public class ScheduleController : ControllerBase
    {
        private readonly ILogger<ScheduleController> _logger;
        private readonly SqlContext _sqlContext;
        private readonly ISessionHelper _sessionHelper;
        public ScheduleController(ILogger<ScheduleController> logger, SqlContext sqlContext, ISessionHelper sessionHelper)
        {
            _logger = logger;
            _sqlContext = sqlContext;
            _sessionHelper = sessionHelper;
        }

        [HttpGet("{flatId}", Name = nameof(GetSchedulesByMonth))]
        public async Task<ActionResult<Dictionary<DateTime, List<ScheduleModel>>>> GetSchedulesByMonth(int flatId, [FromQuery] SchedulesByMonthModel model)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.Users)
                .FirstOrDefaultAsync(x => x.Id == flatId)
                .ConfigureAwait(false);

            if(flat == null || !_sessionHelper.IsUserOfFlat(flat))
            {
                return new BadRequestResult();
            }

            var startDate = new DateTime(model.Year, model.Month, 1);
            var endDate = startDate.AddMonths(1).AddDays(-1);

            var houseworks = await _sqlContext.Houseworks
                .Include(x => x.HouseworkSchedules)
                .Include(x => x.HouseworkSettings)
                .Include(x => x.Users)
                .Where(x => x.FlatId == flatId)
                .ToListAsync()
                .ConfigureAwait(false);

            foreach(var housework in houseworks)
            {
                housework.GenerateSchedules(endDate, _sqlContext);
            }

            _sqlContext.SaveChanges();
            
            var schedules = await _sqlContext.HouseworkSchedules
                .Include(x => x.Housework)
                .Include(x => x.User)
                .Include(x => x.Status)
                .Where(x => x.Date >= startDate && x.Date <= endDate)
                .Where(x => x.Housework.FlatId == flatId)
                .ToListAsync()
                .ConfigureAwait(false);

            Dictionary<DateTime, List<ScheduleModel>> result = new();

            while(startDate <= endDate)
            {
                result.Add(startDate, schedules.Where(x => x.Date.Date == startDate.Date).Select(x => x.ToScheduleModel()).ToList());
                startDate = startDate.AddDays(1);
            }

            return result;
        }

        [HttpPatch("{scheduleId}", Name = nameof(PatchSchedule))]
        public async Task<ActionResult> PatchSchedule(int scheduleId, SchedulePatchModel model)
        {
            var schedule = await _sqlContext.HouseworkSchedules
                .Include(x => x.Housework)
                .ThenInclude(x => x.Flat)
                .ThenInclude(x => x.Users)
                .FirstOrDefaultAsync(x => x.Id == scheduleId)
                .ConfigureAwait(false);

            if(schedule == null)
            {
                return new BadRequestResult();
            }


            if(model.UserId != null)
            {
                if(!_sessionHelper.IsCreatorOfFlat(schedule.Housework.Flat) && model.UserId != _sessionHelper.UserId)
                {
                    return new BadRequestResult();
                }

                schedule.UserId = (int)model.UserId;
            }

            if(model.StatusId != null)
            {
                if(!_sessionHelper.IsCreatorOfFlat(schedule.Housework.Flat) && schedule.UserId != _sessionHelper.UserId)
                {
                    return new BadRequestResult();
                }

                if(!Enum.IsDefined(typeof(Consts.HouseworkStatuses), model.StatusId))
                {
                    return new BadRequestResult();
                }

                schedule.StatusId = (int)model.StatusId;
            }

            if(model.Date != null)
            {
                if (!_sessionHelper.IsCreatorOfFlat(schedule.Housework.Flat))
                {
                    return new BadRequestResult();
                }

                schedule.Date = (DateTime)model.Date;
            }

            _sqlContext.SaveChanges();

            return Ok();
        }
    }
}
