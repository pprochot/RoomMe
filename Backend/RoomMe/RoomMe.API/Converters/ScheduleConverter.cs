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
        public static ScheduleFullGetModel ToScheduleFullGetModel(this HouseworkSchedule schedule)
        {
            return new ScheduleFullGetModel()
            {
                Id = schedule.Id,
                Housework = schedule.Housework.ToHouseworkModel(),
                User = schedule.User.ToUserNicknameModel(),
                Date = schedule.Date,
                Status = schedule.HouseworkStatus.ToHouseworkStatusModel(),
                Settings = schedule.Housework.HouseworkSettings.ToHouseworkSettingsModel()
            };
        }

        public static HouseworkSchedule ToScheduleModel(this SchedulePutModel schedule, Housework housework)
        {
            return new HouseworkSchedule()
            {
                HouseworkId = schedule.HouseworkId,
                Housework = housework,
                //TODO: UserId received through JWT token
                Date = schedule.Date,
            };
        }

        public static SchedulePutReturnModel ToSchedulePutReturnModel(this HouseworkSchedule schedule)
        {
            return new SchedulePutReturnModel()
            {
                Id = schedule.Id,
                TimeStamp = DateTime.Now
            };
        }

        public static ScheduleDateModel ToScheduleDateModel(this HouseworkSchedule schedule)
        {
            return new ScheduleDateModel()
            {
                Id = schedule.Id,
                Date = schedule.Date
            };
        }

    }
}
