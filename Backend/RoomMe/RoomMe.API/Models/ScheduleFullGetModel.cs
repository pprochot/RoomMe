using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ScheduleFullGetModel
    {
        public int Id { get; set; }
        public int HouseworkId { get; set; }
        public HouseworkNameModel Housework { get; set; }
        public UserNicknameModel User { get; set; }
        public DateTime Date { get; set; }
        public int StatusId { get; set; }
        public HouseworkStatusModel Status { get; set; }
        public int SettingsId { get; set; }
        public HouseworkSettingsModel Settings { get; set; }
    }
}
