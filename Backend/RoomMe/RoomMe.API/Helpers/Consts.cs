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

        public const string FilePath = "/receipts/";

        public enum HouseworkFrequencies { 
            Once = 1,
            Daily = 2,
            Weekly = 3,
            TwiceAWeek = 4,
        }

        public enum HouseworkStatuses
        {
            ToDo = 1,
            Done = 2,
            Expired = 3,
            Delayed = 4,
        }
    }
}
