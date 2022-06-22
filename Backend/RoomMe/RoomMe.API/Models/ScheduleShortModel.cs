using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ScheduleShortModel
    {
        public int Id { get; set; }
        public DateTime Date { get; set; }
        public HouseworkStatusModel Status { get; set; }
        public UserNicknameModel User { get; set; }
    }
}
