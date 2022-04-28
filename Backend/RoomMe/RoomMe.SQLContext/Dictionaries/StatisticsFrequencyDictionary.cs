using Microsoft.EntityFrameworkCore;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Dictionaries
{
    public static class StatisticsFrequencyDictionary
    {
        public static void AddRecords(ModelBuilder modelbuilder)
        {
            modelbuilder.Entity<StatisticsFrequency>().HasData(
                new { Id = 1, Name = "All costs" },
                new { Id = 2, Name = "Daily" },
                new { Id = 3, Name = "Weekly" },
                new { Id = 4, Name = "Monthly" }
            );
        }
    }
}
