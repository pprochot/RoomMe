using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Dictionaries
{
    static class DictionaryBuilder
    {
        public static void AddDictionaries(ModelBuilder modelBuilder)
        {
            HouseworkStatusesDictionary.AddRecords(modelBuilder);
            HouseworkFrequencyDictionary.AddRecords(modelBuilder);
            StatisticsFrequencyDictionary.AddRecords(modelBuilder);
        }
    }
}
