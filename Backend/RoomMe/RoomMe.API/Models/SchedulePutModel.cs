﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class SchedulePutModel
    {
        public int HouseworkId { get; set; }
        public DateTime Date { get; set; }
    }
}