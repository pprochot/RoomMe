using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ProductListPostReturnModel
    {
        public IEnumerable<int> ProductIds { get; set; }
        public DateTime CreationDate { get; set; }
    }
}
