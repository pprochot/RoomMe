using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class HouseworkPostModel
    {
        public string Name { get; set; }
        public int FlatId { get; set; }
        public string Description { get; set; }
        public List<int> Users { get; set; }
        public int FrequencyId { get; set; }
        public int[] Days { get; set; }
    }
}
