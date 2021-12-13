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
    }
}
