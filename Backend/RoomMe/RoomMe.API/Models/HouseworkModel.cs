using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class HouseworkModel
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public FlatShortModel Flat { get; set; }
        public UserNicknameModel Author { get; set; }
        public string Description { get; set; }
        public List<UserNicknameModel> Users { get; set; }
        public ScheduleShortModel NextSchedule { get; set; }
        public HouseworkSettingsModel Settings { get; set; }
    }
}
