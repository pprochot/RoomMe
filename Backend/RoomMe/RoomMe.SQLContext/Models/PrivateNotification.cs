using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Models
{
    public class PrivateNotification
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public User User { get; set; }
        public int SettingsId { get; set; }
        public NotificationSettings Settings { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
    }
}
