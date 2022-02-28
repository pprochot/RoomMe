using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Extensions
{
    public static class RefreshTokenExtensions
    {
        public static bool IsExpired(this RefreshToken token) => DateTime.UtcNow >= token.Expires;
        public static bool IsRevoked(this RefreshToken token) => token.Revoked != null;
        public static bool IsActive(this RefreshToken token) => !token.IsRevoked() && !token.IsExpired();
    }
}
