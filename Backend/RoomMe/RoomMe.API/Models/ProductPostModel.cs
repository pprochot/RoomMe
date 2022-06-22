using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class  ProductPostModel
    {
        public string Name { get; set; }
        public string Description { get; set; }
        public string Reason { get; set; }
        public int Quantity { get; set; }
    }
}
