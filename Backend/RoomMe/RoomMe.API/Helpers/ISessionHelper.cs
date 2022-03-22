using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Helpers
{
    public interface ISessionHelper
    {
        public int UserId { get; }
        public User Session { get; }
        public List<int> FriendsIds { get; }

        public bool IsUserOfFlat(Flat flat);
        public bool IsCreatorOfFlat(Flat flat);
    }
}
