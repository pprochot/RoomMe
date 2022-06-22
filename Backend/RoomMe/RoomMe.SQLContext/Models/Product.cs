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
        public int ShoppingListId { get; set; }
        public ShoppingList ShoppingList { get; set; }
        public int AuthorId { get; set; }
        public User Author { get; set; }
        public int? CommonCostId { get; set; }
        public CommonCost CommonCost { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public string Reason { get; set; }
        public int Quantity { get; set; }
        public bool Bought { get; set; }
    }
}
