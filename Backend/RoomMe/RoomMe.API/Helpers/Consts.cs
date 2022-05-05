using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Helpers
{
    public static class Consts
    {
        public const int MaxNickLength = 24;
        public const int MinNickLength = 4;
        public const int MinPasswordLength = 6;
        public const int MaxPasswordLength = 24;

        public const int AllStatsId = 1;
        public const int DailyStatsId = 2;
        public const int WeeklyStatsId = 3;
        public const int MonthlyStatsId = 4;

        public const string FilePath = "/receipts/";
    }
}
