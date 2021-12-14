using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class FlatPostModel
    {
        public string Name { get; set; }
        public string Address { get; set; }
        public List<int> Users { get; set; }
    }
}
