using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Models
{
    public class User
    {
        public int Id { get; set; }
        public string Nickname { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string Firstname { get; set; }
        public string Lastname { get; set; }
        public string PhoneNumber { get; set; }
        public List<Flat> Flats { get; set; }
        public List<Housework> Houseworks { get; set; }
        public List<PrivateNotification> PrivateNotifications { get; set; }
        public List<FlatNotification> FlatNotifications { get; set; }
    }
}
