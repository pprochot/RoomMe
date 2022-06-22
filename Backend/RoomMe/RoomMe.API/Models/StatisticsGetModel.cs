using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class StatisticsGetModel
    {
        public DateTime From { get; set; }
        public DateTime To { get; set; }
        public int frequencyId { get; set; }
    }
}
