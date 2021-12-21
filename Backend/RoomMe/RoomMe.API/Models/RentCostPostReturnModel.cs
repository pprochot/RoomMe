using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class RentCostPostReturnModel
    {
        public int userId { get; set; }
        public int flatId { get; set; }
        public DateTime CreationDate { get; set; }
    }
}
