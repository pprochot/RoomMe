using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class HouseworkPutReturnModel
    {
        public int Id { get; set; }
        public int SettingsId { get; set; }
        public DateTime CreationDate { get; set; }
    }
}
