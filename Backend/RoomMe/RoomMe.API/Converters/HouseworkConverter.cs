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
        public static HouseworkNameModel ToHouseworkNameModel(this Housework housework)
        {
            return new HouseworkNameModel()
            {
                Id = housework.Id,
                Name = housework.Name
            };
        }

        public static HouseworkFullGetModel ToHouseworkFullModel(this Housework housework)
        {
            return new HouseworkFullGetModel
            {
                Id = housework.Id,
                Name = housework.Name,
                Flat = housework.Flat.ToFlatNameModel(),
                Author = housework.Author.ToUserNicknameModel(),
                Description = housework.Description,
                Users = housework.Users.Select(x => x.ToUserNicknameModel()).ToList()
                //schedules
            };
        }

        public static Housework ToHouseworkModel(this HouseworkPutModel housework, User author, Flat flat, List<User> users)
        {
            return new Housework()
            {
                Name = housework.Name,
                FlatId = housework.FlatId,
                Flat = flat,
                AuthorId = housework.AuthorId,
                Author = author,
                Description = housework.Description,
                Users = users
                //schedules
            };
        }

        public static HouseworkPutReturnModel ToHouseworkPutReturnModel(this Housework housework)
        {
            return new HouseworkPutReturnModel()
            {
                Id = housework.Id,
                CreationDate = DateTime.Now
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

        public static HouseworkFrequencyModel ToHouseworkFrequenciesModel(this HouseworkFrequency frequency)
        {
            return new HouseworkFrequencyModel()
            {
                Id = frequency.Id,
                Name = frequency.Name,
                Value = frequency.Value
            };
        }

        public static HouseworkSettingsModel ToHouseworkSettingsModel(this HouseworkSettings settings)
        {
            return new HouseworkSettingsModel()
            {
                Id = settings.Id,
                HouseworkId = settings.HouseworkId,
                Housework = settings.Housework.ToHouseworkNameModel(),
                FrequencyId = settings.FrequencyId,
                Frequency = settings.Frequency.ToHouseworkFrequenciesModel(),
                Day = settings.Day
            };
        }
    }
}
