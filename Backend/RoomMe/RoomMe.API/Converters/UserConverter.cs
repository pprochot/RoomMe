using RoomMe.API.Models;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Converters
{
    public static class UserConverter
    {
        public static UserNicknameModel ToUserNicknameModel(this User user)
        {
            return new UserNicknameModel()
            {
                Id = user.Id,
                Nickname = user.Nickname
            };
        }

        public static User ToUser(this UserPostModel user)
        {
            return new User()
            {
                Nickname = user.Nickname,
                Email = user.Email,
                Password = user.Password,
                Firstname = user.Firstname,
                Lastname = user.Lastname,
                PhoneNumber = user.PhoneNumber
            };
        }
    }
}
