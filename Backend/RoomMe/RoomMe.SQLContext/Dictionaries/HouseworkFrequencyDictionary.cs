using Microsoft.EntityFrameworkCore;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Dictionaries
{
	static class HouseworkFrequencyDictionary
	{
		public static void AddRecords(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<HouseworkFrequency>().HasData(
                new { Id = 1, Name = "Once", Value = 0 },
                new { Id = 2, Name = "Daily", Value = 1 },
                new { Id = 3, Name = "Weekly", Value = 7 },
                new { Id = 4, Name = "Twice a week", Value = 3},
                new { Id = 5, Name = "Monthly", Value = 30 }
                );
        }
	}
}


