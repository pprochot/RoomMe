using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Models
{
    public class Product
    {
        public int Id { get; set; }
        public int ShoppingListID { get; set; }
        public User AuthorId { get; set; }
        public int CommonCostId { get; set; }
        public String Name { get; set; }
        public String Description { get; set; }
        public String Reason { get; set; }
        public String Quantity { get; set; }
        public String Bought { get; set; }

    }
}
