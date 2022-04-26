using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ScheduleModel
    {
        public int Id { get; set; }
        public HouseworkShortModel Housework { get; set; }
        public UserNicknameModel User { get; set; }
        public DateTime Date { get; set; }
        public HouseworkStatusModel Status { get; set; }
    }
}
