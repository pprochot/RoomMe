using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Models
{
    public class HouseworkSchedule
    {
        public int Id { get; set; }
        public int HouseworkId { get; set; }
        public Housework Housework { get; set; }
        public int UserId { get; set; }
        public User User { get; set; }
        public int StatusId { get; set; }
        public HouseworkStatus HouseworkStatus { get; set; }
        public DateTime Date { get; set; }
    }
}
