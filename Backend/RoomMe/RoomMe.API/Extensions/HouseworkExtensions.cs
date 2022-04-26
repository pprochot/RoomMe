﻿using RoomMe.API.Helpers;
using RoomMe.SQLContext;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Extensions
{
    public static class HouseworkExtensions
    {
        public static void GenerateSchedules(this Housework housework, DateTime untilDate, SqlContext sqlContext)
        {
            var availableDays = housework.HouseworkSettings.Days.Split(",").Select(x => int.Parse(x)).ToArray();

            if(availableDays.Length == 0)
            {
                return;
            }

            if (housework.HouseworkSettings.FrequencyId == (int)Consts.HouseworkFrequencies.Once)
            {
                var schedule = housework.HouseworkSchedules.LastOrDefault();

                if(schedule != null)
                {
                    return;
                }

                int diff = availableDays[0] - (int)DateTime.UtcNow.DayOfWeek;
                if (diff <= 0) diff += 7;

                housework.HouseworkSchedules.Add(new HouseworkSchedule()
                {
                    HouseworkId = housework.Id,
                    UserId = housework.Users.First().Id,
                    StatusId = 1,
                    Date = DateTime.UtcNow.AddDays(diff)
                });
            }
            else
            {
                var lastSchedule = housework.HouseworkSchedules.OrderByDescending(x => x.Date).FirstOrDefault();

                if (lastSchedule != null && lastSchedule.Date > untilDate)
                {
                    return;
                }


                var differences = new int[availableDays.Length];

                for (int i = 0; i < availableDays.Length; i++)
                {
                    if (i + 1 == availableDays.Length)
                    {
                        differences[i] = 7 + availableDays[0] - availableDays[i];
                    }
                    else
                    {
                        differences[i] = availableDays[i + 1] - availableDays[i];
                    }
                }

                int diffDate = availableDays[0] - (int)DateTime.UtcNow.DayOfWeek;
                if (diffDate <= 0) diffDate += 7;
                DateTime currDate = DateTime.UtcNow.AddDays(diffDate);

                if (lastSchedule != null)
                {
                    diffDate = availableDays[0] - (int)lastSchedule.Date.DayOfWeek;
                    if (diffDate <= 0) diffDate += 7;
                    currDate = lastSchedule.Date.AddDays(diffDate);
                }

                var users = housework.Users.ToArray();
                int userIndex = 0;
                int diffIndex = 0;

                while (currDate < untilDate)
                {
                    housework.HouseworkSchedules.Add(new HouseworkSchedule()
                    {
                        HouseworkId = housework.Id,
                        UserId = users[userIndex].Id,
                        StatusId = 1,
                        Date = currDate
                    });

                    diffIndex = (diffIndex + 1) % availableDays.Length;
                    userIndex = (userIndex + 1) % users.Length;
                    diffDate = availableDays[diffIndex] - (int)currDate.DayOfWeek;
                    if (diffDate <= 0) diffDate += 7;

                    currDate = currDate.AddDays(diffDate);
                }
            }

            sqlContext.SaveChanges();
        }
    }
}
