using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using RoomMe.API.Authorization;
using RoomMe.API.Helpers;
using RoomMe.API.Models;
using RoomMe.API.Converters;
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
    public class StatisticsController : ControllerBase
    {
        private readonly ILogger<StatisticsController> _logger;
        private readonly SqlContext _sqlContext;
        private readonly ISessionHelper _sessionHelper;

        public StatisticsController(ILogger<StatisticsController> logger, SqlContext sqlContext, ISessionHelper sessionHelper)
        {
            _logger = logger;
            _sqlContext = sqlContext;
            _sessionHelper = sessionHelper;
        }

        [HttpGet("flat/{flatId}", Name = nameof(GetCommonCostsStatistics))]
        public async Task<ActionResult<IEnumerable<StatisticsReturnModel>>> GetCommonCostsStatistics(int flatId, [FromQuery] StatisticsGetModel args)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.Users)
                .SingleOrDefaultAsync(x => x.Id == flatId)
                .ConfigureAwait(false);

            if(flat == null || !_sessionHelper.IsUserOfFlat(flat))
            {
                return new BadRequestResult();
            }

            var costs = await _sqlContext.CommonCosts
                .Where(x => x.Date >= args.From && x.Date <= args.To)
                .ToListAsync()
                .ConfigureAwait(false);

            var res = new List<StatisticsReturnModel>();

            if (args.frequencyId == Consts.AllStatsId)
            {
                res = costs.Select(x => x.ToStatisticsReturnModel()).ToList();
            }
            else if (args.frequencyId == Consts.DailyStatsId || args.frequencyId == Consts.WeeklyStatsId || args.frequencyId == Consts.MonthlyStatsId)
            {
                var rangeLength = args.frequencyId == Consts.WeeklyStatsId ? 7 : 1;
                var ranges = args.frequencyId == Consts.MonthlyStatsId ? GenerateMonthTimeRanges(args.From, args.To) : GenerateDaysTimeRanges(args.From, args.To, rangeLength);
                var currCostId = 0;

                for(var i = 0; i < ranges.Count - 1; i++)
                {
                    double value = 0;

                    while (currCostId < costs.Count && costs[currCostId].Date < ranges[i + 1])
                    {
                        value += costs[currCostId].Value;
                        currCostId++;
                    }

                    res.Add(StatisticsConverter.ToStatisticsReturnModel(ranges[i], value));
                }
            }
            else
            {
                return new BadRequestResult();
            }

            return res;
        }

        [HttpGet("", Name = nameof(GetPrivateCostsStatistics))]
        public async Task<ActionResult<IEnumerable<StatisticsReturnModel>>> GetPrivateCostsStatistics([FromQuery] StatisticsGetModel args)
        {
            var userId = _sessionHelper.UserId;
            var costs = await _sqlContext.PrivateCosts
                .Where(x => x.Date >= args.From && x.Date <= args.To && x.UserId == userId)
                .ToListAsync()
                .ConfigureAwait(false);

            var res = new List<StatisticsReturnModel>();

            if (args.frequencyId == Consts.AllStatsId)
            {
                res = costs.Select(x => x.ToStatisticsReturnModel()).ToList();
            }
            else if (args.frequencyId == Consts.DailyStatsId || args.frequencyId == Consts.WeeklyStatsId || args.frequencyId == Consts.MonthlyStatsId)
            {
                var rangeLength = args.frequencyId == Consts.WeeklyStatsId ? 7 : 1;
                var ranges = args.frequencyId == Consts.MonthlyStatsId ? GenerateMonthTimeRanges(args.From, args.To) : GenerateDaysTimeRanges(args.From, args.To, rangeLength);
                var currCostId = 0;

                for (var i = 0; i < ranges.Count - 1; i++)
                {
                    double value = 0;

                    while (currCostId < costs.Count && costs[currCostId].Date < ranges[i + 1])
                    {
                        value += costs[currCostId].Value;
                        currCostId++;
                    }

                    res.Add(StatisticsConverter.ToStatisticsReturnModel(ranges[i], value));
                }
            }
            else
            {
                return new BadRequestResult();
            }

            return res;
        }

        [AllowAnonymous]
        [HttpGet("frequencies", Name = nameof(GetStatisticsFrequencies))]
        public async Task<IEnumerable<StatisticsFrequency>> GetStatisticsFrequencies()
        {
            return await _sqlContext.StatisticsFrequencies.ToListAsync().ConfigureAwait(false);
        }

        private List<DateTime> GenerateDaysTimeRanges(DateTime from, DateTime to, int lengthOfRange)
        {
            List<DateTime> res = new();

            DateTime curr = from;

            while(curr < to)
            {
                curr = curr.AddDays(lengthOfRange);
                res.Add(curr);
            }

            return res;
        }

        private List<DateTime> GenerateMonthTimeRanges(DateTime from, DateTime to)
        {
            List<DateTime> res = new();

            var start = DateTime.Parse($"01.{from.Month}.{from.Year}");
            var curr = start;

            while(curr < to)
            {
                curr = curr.AddMonths(1);
                res.Add(curr);
            }

            return res;
        }
    }
}
