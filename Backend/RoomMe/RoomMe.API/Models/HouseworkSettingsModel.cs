﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class HouseworkSettingsModel
    {
        public int Id { get; set; }
        public int FrequencyId { get; set; }
        public HouseworkFrequencyModel Frequency { get; set; }
        public int Day { get; set; }
    }
}
