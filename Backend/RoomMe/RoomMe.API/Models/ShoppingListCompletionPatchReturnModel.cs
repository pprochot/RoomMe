using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ShoppingListCompletionPatchReturnModel
    {
        public DateTime TimeStamp { get; set; }
        public IEnumerable<Guid> FileGuids { get; set; }
    }
}
