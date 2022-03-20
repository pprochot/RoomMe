using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Helpers
{
    public class SessionHelper : ISessionHelper
    {
        private readonly IHttpContextAccessor _httpContextAccessor;
        private readonly IConfiguration _configuration;

        public SessionHelper(IHttpContextAccessor httpContextAccessor, IConfiguration configuration)
        {
            _httpContextAccessor = httpContextAccessor;
            _configuration = configuration;
        }

        public User Session => (User)_httpContextAccessor.HttpContext.Items["User"];

        public int UserId => Session.Id;

        public List<int> FriendsIds => Session.Friends.Select(x => x.FriendId).ToList();
    }
}
