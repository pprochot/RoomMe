﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class SchedulePostModel
    {
        public int HouseworkId { get; set; }
        public int UserId { get; set; }
        public DateTime Date { get; set; }
    }
}
