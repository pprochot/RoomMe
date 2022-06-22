using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ShoppingListPostModel
    {
        public int FlatId { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
    }
}
