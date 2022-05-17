using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Models
{
    public class HouseworkSettings
    {
        public int Id { get; set; }
        public int HouseworkId { get; set; }
        public Housework Housework { get; set; }
        public int FrequencyId { get; set; }
        public HouseworkFrequency Frequency { get; set; }
        public string Days { get; set; }
    }
}
