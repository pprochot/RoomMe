using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ScheduleHouseworkNameModel
    {
        public int Id { get; set; }
        public int HouseworkId { get; set; }
        public HouseworkNameModel Housework { get; set; }
    }
}
