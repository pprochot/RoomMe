using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class SchedulePatchModel
    {
        public int? UserId { get; set; }
        public DateTime? Date { get; set; }
        public int? StatusId { get; set; }
    }
}
