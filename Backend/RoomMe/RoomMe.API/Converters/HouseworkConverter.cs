﻿using RoomMe.API.Models;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Converters
{
    public static class HouseworkConverter
    {

        public static HouseworkFullGetModel ToHouseworkFullModel(this Housework housework)
        {
            return new HouseworkFullGetModel
            {
                Id = housework.Id,
                Name = housework.Name,
                Flat = housework.Flat.ToFlatNameModel(),
                Author = housework.Author.ToUserNicknameModel(),
                Description = housework.Description,
                Users = housework.Users.Select(x => x.ToUserNicknameModel()).ToList(),
                Schedules = housework.HouseworkSchedules.Select(x => x.ToScheduleDateModel()).ToList()
            };
        }

        public static Housework ToHouseworkModel(this HouseworkPutModel housework)
        {
            return new Housework()
            {
                Name = housework.Name,
                FlatId = housework.FlatId,
                AuthorId = housework.AuthorId,
                Description = housework.Description,
            };
        }

        public static HouseworkPutReturnModel ToHouseworkPutReturnModel(this Housework housework, int settingsId)
        {
            return new HouseworkPutReturnModel()
            {
                Id = housework.Id,
                SettingsId = settingsId,
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

        public static HouseworkSettingsModel ToHouseworkSettingsModel(this HouseworkSettings settings)
        {
            return new HouseworkSettingsModel()
            {
                Id = settings.Id,
                FrequencyId = settings.FrequencyId,
                Frequency = settings.Frequency.ToHouseworkFrequencyModel(),
                Day = settings.Day
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

        public static HouseworkFrequency ToHouseworkFrequency(this HouseworkFrequencyPutModel frequency)
        {
            return new HouseworkFrequency()
            {
                Name = frequency.Name,
                Value = frequency.Value
            };
        }

        public static HouseworkSettings ToHouseworkSettings(this HouseworkSettingsPutModel settings)
        {
            return new HouseworkSettings()
            {
                HouseworkId = settings.HouseworkId,
                FrequencyId = settings.FrequencyId,
                Day = settings.Day,
            };
        }
    }
}
