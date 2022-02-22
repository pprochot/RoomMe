using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Helpers
{
    public interface ISessionHelper
    {
        public int UserId();
        public User Session { get; }
    }
}
