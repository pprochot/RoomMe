using Microsoft.EntityFrameworkCore;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Dictionaries
{
    static class HouseworkStatusesDictionary
    {
        public static void AddRecords(ModelBuilder modelbuilder)
        {
            modelbuilder.Entity<HouseworkStatus>().HasData(
                new { Id = 1, Name = "Todo" },
                new { Id = 2, Name = "Done" },
                new { Id = 3, Name = "Expired" },
                new { Id = 4, Name = "Delayed" }
                );
        }
    }
}
