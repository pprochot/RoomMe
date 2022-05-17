using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class HouseworkSettingsModel
    {
        public int Id { get; set; }
        public HouseworkFrequencyModel Frequency { get; set; }
        public int[] Days { get; set; }
    }
}
