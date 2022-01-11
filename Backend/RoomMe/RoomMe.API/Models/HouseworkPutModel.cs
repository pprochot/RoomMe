using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class HouseworkPutModel
    {
        public string Name { get; set; }
        public int FlatId { get; set; }
        public int AuthorId { get; set; }
        public string Description { get; set; }
        public List<UserNicknameModel> Users { get; set; }
    }
}
