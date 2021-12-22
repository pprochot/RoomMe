using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ShoppingListGetModel
    {
        public int Id { get; set; }
        public int FlatId { get; set; }
        public int? CompletorId { get; set; }
        public string CompletorName { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public DateTime CreationDate { get; set; }
        public DateTime CompletionDate { get; set; }
        public List<ProductModel> Products { get; set; }
    }
}
