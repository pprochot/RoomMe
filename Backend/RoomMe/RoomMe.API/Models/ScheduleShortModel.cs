using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ScheduleShortModel
    {
        public int Id { get; set; }
        public UserNicknameModel User;
        public DateTime Date { get; set; }
        public HouseworkStatusModel Status { get; set; }
    }
}
