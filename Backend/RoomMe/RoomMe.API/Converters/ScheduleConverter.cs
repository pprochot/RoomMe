using RoomMe.API.Models;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Converters
{
    public static class ScheduleConverter
    {
        public static ScheduleModel ToScheduleModel(this HouseworkSchedule schedule)
        {
            return new ScheduleModel()
            {
                Id = schedule.Id,
                Housework = schedule.Housework.ToHouseworkShortModel(),
                User = schedule.User.ToUserNicknameModel(),
                Date = schedule.Date,
                Status = schedule.Status.ToHouseworkStatusModel(),
            };
        }

        public static HouseworkSchedule ToHouseworkSchedule(this SchedulePostModel schedule, Housework housework, int userId)
        {
            return new HouseworkSchedule()
            {
                HouseworkId = schedule.HouseworkId,
                Housework = housework,
                StatusId = 1,
                UserId = userId,
                Date = schedule.Date,
            };
        }

        public static SchedulePostReturnModel ToSchedulePostReturnModel(this HouseworkSchedule schedule)
        {
            return new SchedulePostReturnModel()
            {
                Id = schedule.Id,
                Date = schedule.Date,
            };
        }

        public static ScheduleShortModel ToScheduleShortModel(this HouseworkSchedule schedule)
        {
            return new ScheduleShortModel()
            {
                Id = schedule.Id,
                User = schedule.User.ToUserNicknameModel(),
                Date = schedule.Date,
                StatusId = schedule.StatusId
            };
        }
    }
}
