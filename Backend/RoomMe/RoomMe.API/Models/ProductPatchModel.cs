using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ProductPatchModel
    {
        public int Id { get; set; }
        public double Value { get; set; }
        public string Description { get; set; }
    }
}
