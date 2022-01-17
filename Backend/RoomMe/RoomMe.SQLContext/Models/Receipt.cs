using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Models
{
    public class Receipt
    {
        public int Id { get; set; }
        public Guid Guid { get; set; }
        public int ShoppingListId { get; set; }
        public string Name { get; set; }
        public string Extension { get; set; }
        public string Path { get; set; }
    }
}
