using RoomMe.API.Models;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Converters
{
    public static class StatisticsConverter
    {
        public static StatisticsReturnModel ToStatisticsReturnModel(this CommonCost cost)
        {
            return new StatisticsReturnModel()
            {
                TimeStamp = cost.Date,
                Value = cost.Value,
            };
        }

        public static StatisticsReturnModel ToStatisticsReturnModel(DateTime timeStamp, double value)
        {
            return new StatisticsReturnModel()
            {
                TimeStamp = timeStamp,
                Value = value,
            };
        }

        public static StatisticsReturnModel ToStatisticsReturnModel(this PrivateCost cost)
        {
            return new StatisticsReturnModel()
            {
                TimeStamp = cost.Date,
                Value = cost.Value,
            };
        }
    }
}
