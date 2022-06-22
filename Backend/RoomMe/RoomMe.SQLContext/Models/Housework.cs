using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Models
{
    public class Housework
    {
        public int Id { get; set; }
        public int FlatId { get; set; }
        public Flat Flat { get; set; }
        public int AuthorId { get; set; }
        public User Author { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public List<User> Users { get; set; }
        public List<HouseworkSchedule> HouseworkSchedules { get; set; }
        public HouseworkSettings HouseworkSettings { get; set; }
    }
}
