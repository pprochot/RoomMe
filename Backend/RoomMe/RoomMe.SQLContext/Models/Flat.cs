using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Models
{
    public class Flat
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Address { get; set; }
        public List<User> Users { get; set; }
        public List<Housework> Houseworks { get; set; }
        public List<FlatNotification> Notifications { get; set; }
        public List<CommonCost> Costs { get; set; }
        public List<RentCost> RentCosts { get; set; }
    }
}
