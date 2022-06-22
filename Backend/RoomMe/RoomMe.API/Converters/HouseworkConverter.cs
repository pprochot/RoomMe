using RoomMe.API.Models;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Converters
{
    public static class HouseworkConverter
    {
        public static HouseworkModel ToHouseworkModel(this Housework housework)
        {
            return new HouseworkModel
            {
                Id = housework.Id,
                Name = housework.Name,
                Flat = housework.Flat.ToFlatNameModel(),
                Author = housework.Author.ToUserNicknameModel(),
                Description = housework.Description,
                Users = housework.Users.Select(x => x.ToUserNicknameModel()).ToList(),
                Settings = housework.HouseworkSettings.ToHouseworkSettingsModel(),
                NextSchedule = housework.HouseworkSchedules.First(x => x.Date > DateTime.Now).ToScheduleShortModel()
            };
        }

        public static HouseworkShortModel ToHouseworkShortModel(this Housework housework)
        {
            return new HouseworkShortModel
            {
                Id = housework.Id,
                Name = housework.Name,
                Description = housework.Description,
            };
        }

        public static Housework ToHouseworkModel(this HouseworkPostModel housework, int authorId, List<User> users)
        {
            return new Housework()
            {
                Name = housework.Name,
                FlatId = housework.FlatId,
                Users = users,
                AuthorId = authorId,
                Description = housework.Description,
                HouseworkSchedules = new List<HouseworkSchedule>()
            };
        }

        public static HouseworkPostReturnModel ToHouseworkPutReturnModel(this Housework housework, int settingsId)
        {
            return new HouseworkPostReturnModel()
            {
                Id = housework.Id,
                SettingsId = settingsId,
                CreationDate = DateTime.UtcNow
            };
        }

        public static HouseworkStatusModel ToHouseworkStatusModel(this HouseworkStatus status)
        {
            return new HouseworkStatusModel()
            {
                Id = status.Id,
                Name = status.Name
            };
        }

        public static HouseworkSettingsModel ToHouseworkSettingsModel(this HouseworkSettings settings)
        {
            return new HouseworkSettingsModel()
            {
                Id = settings.Id,
                Frequency = settings.Frequency.ToHouseworkFrequencyModel(),
                Days = settings.Days.Split(",").Select(x => int.Parse(x)).ToArray()
            };
        }


        public static HouseworkFrequencyModel ToHouseworkFrequencyModel(this HouseworkFrequency frequency)
        {
            return new HouseworkFrequencyModel()
            {
                Id = frequency.Id,
                Name = frequency.Name,
                Value = frequency.Value
            };
        }
        public static void UpdateHousework(this Housework houseworkEntity, HouseworkPostModel housework, List<User> users, int authorId)
        {
            houseworkEntity.AuthorId = authorId;
            houseworkEntity.Name = housework.Name;
            houseworkEntity.Description = housework.Description;
            houseworkEntity.FlatId = housework.FlatId;
            houseworkEntity.Users = users;
            houseworkEntity.HouseworkSettings.FrequencyId = housework.FrequencyId;
            houseworkEntity.HouseworkSettings.Days = string.Join(",", housework.Days);
        }
    }
}
