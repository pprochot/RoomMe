using RoomMe.API.Helpers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class ApiResult<T>
    {
        public bool Result { get; set; }
        public string ErrorName { get; set; }
        public T Value { get; set; }
    }
}
