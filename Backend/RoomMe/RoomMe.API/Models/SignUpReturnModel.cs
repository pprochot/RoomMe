using RoomMe.API.Helpers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class SignUpReturnModel
    {
        public bool Result { get; set; }
        public ErrorCodes.SignUpErrors? ErrorCode { get; set; }
        public int? UserId { get; set; }
    }
}
