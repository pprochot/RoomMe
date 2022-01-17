using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ProductPatchReturnModel
    {
        public DateTime TimeStamp { get; set; }
        public IEnumerable<int> CommonCostIds { get; set; }
    }
}
