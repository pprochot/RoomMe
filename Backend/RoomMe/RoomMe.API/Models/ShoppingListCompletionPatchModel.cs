using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ShoppingListCompletionPatchModel
    {
        public string fileName { get; set; }
        public string Extension { get; set; }
        public byte[] fileContent { get; set; }
        public string MimeType { get; set; }
    }
}
