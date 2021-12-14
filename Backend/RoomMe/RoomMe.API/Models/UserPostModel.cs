﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class UserPostModel
    {
        public string Nickname { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string Firstname { get; set; }
        public string Lastname { get; set; }
        public string PhoneNumber { get; set; }
    }
}
