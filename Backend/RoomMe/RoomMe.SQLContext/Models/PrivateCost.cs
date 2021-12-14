﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.SQLContext.Models
{
    public class PrivateCost
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public User User { get; set; }
        public double Value { get; set; }
        public string Description { get; set; }
        public DateTime Date { get; set; }
    }
}