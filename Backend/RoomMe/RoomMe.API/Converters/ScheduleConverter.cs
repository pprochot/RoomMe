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
        public static ScheduleHouseworkNameModel ToScheduleHouseworkNameModel(this HouseworkSchedule schedule)
        {
            return new ScheduleHouseworkNameModel()
            {
                Id = schedule.Id,
                HouseworkId = schedule.HouseworkId,
                Housework = schedule.Housework.ToHouseworkNameModel()
            };
        }

        public static ScheduleFullGetModel ToScheduleFullGetModel(this HouseworkSchedule schedule)
        {
            return new ScheduleFullGetModel()
            {
                Id = schedule.Id,
                HouseworkId = schedule.HouseworkId,
                Housework = schedule.Housework.ToHouseworkNameModel(),
                User = schedule.User.ToUserNicknameModel(),
                Date = schedule.Date,
                StatusId = schedule.StatusId,
                Status = schedule.HouseworkStatus.ToHouseworkStatusModel(),
                SettingsId = schedule.HouseworkSettings.Id,
                Settings = schedule.HouseworkSettings.ToHouseworkSettingsModel()
            };
        }

        public static HouseworkSchedule ToScheduleModel(this SchedulePutModel schedule, Housework housework, User user, 
            HouseworkStatus status)
        {
            return new HouseworkSchedule()
            {
                HouseworkId = schedule.HouseworkId,
                Housework = housework,
                UserId = schedule.UserId,
                User = user,
                Date = schedule.Date,
                StatusId = schedule.StatusId,
                HouseworkStatus = status,
            };
        }

        public static SchedulePutReturnModel ToSchedulePutReturnModel(this HouseworkSchedule schedule)
        {
            return new SchedulePutReturnModel()
            {
                Id = schedule.Id
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
