using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Models
{
    public class ShoppingList
    {
        public int Id { get; set; }
        public int FlatId { get; set; }
        public Flat Flat { get; set; }
        public int? CompletorId { get; set; }
        public User Completor { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public DateTime CreationDate { get; set; }
        public DateTime CompletionDate { get; set; }
        public List<Receipt> Receipts { get; set; }
        public List<Product> Products { get; set; }
    }
}
