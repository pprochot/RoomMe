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
                Status = schedule.Status.ToHouseworkStatusModel(),
                Settings = schedule.Housework.HouseworkSettings.ToHouseworkSettingsModel()
            };
        }

        public static HouseworkSchedule ToScheduleModel(this SchedulePutModel schedule, Housework housework, int userId)
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

        public static SchedulePutReturnModel ToSchedulePutReturnModel(this HouseworkSchedule schedule)
        {
            return new SchedulePutReturnModel()
            {
                Id = schedule.Id,
                TimeStamp = DateTime.UtcNow
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

        public static ScheduleListModel ToScheduleListModel(this HouseworkSchedule schedule)
        {
            return new ScheduleListModel()
            {
                Id = schedule.Id,
                HouseworkName = schedule.Housework.Name,
                Date = schedule.Date,
                Status = schedule.Status.ToHouseworkStatusModel(),
                UserId = schedule.UserId,
                UserName = schedule.User.Nickname
            };
        }

    }
}
