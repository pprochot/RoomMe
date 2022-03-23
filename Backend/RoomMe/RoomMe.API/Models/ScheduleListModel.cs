using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ScheduleListModel
    {
        public int Id { get; set; }
        public string HouseworkName { get; set; }
        public DateTime Date { get; set; }
        public HouseworkStatusModel Status { get; set; }
        public int UserId { get; set; }
        public string UserName { get; set; }
    }
}
