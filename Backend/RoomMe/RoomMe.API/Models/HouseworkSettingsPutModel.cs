using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class HouseworkSettingsPutModel
    {
        public int HouseworkId { get; set; }
        public int FrequencyId { get; set; }
        public int Day { get; set; }
    }
}
