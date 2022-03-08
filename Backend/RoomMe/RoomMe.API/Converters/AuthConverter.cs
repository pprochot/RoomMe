using RoomMe.API.Models;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Converters
{
    public static class AuthConverter
    {
        public static SignInReturnModel ToLoginReturnModel(this User user, string token, string refreshToken)
        {
            return new SignInReturnModel
            {
                Id = user.Id,
                Nickname = user.Nickname,
                Email = user.Email,
                Firstname = user.Firstname,
                Lastname = user.Lastname,
                PhoneNumber = user.PhoneNumber,
                AccessToken = token,
                RefreshToken = refreshToken
            };
        }
    }
}
