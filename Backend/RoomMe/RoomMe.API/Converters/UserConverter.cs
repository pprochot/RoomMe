using RoomMe.API.Models;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using BCryptNet = BCrypt.Net;
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

        public static User ToUser(this SignUpUserModel user)
        {
            return new User()
            {
                Nickname = user.Nickname,
                Email = user.Email,
                Password = BCryptNet.BCrypt.EnhancedHashPassword(user.Password),
                Firstname = user.Firstname,
                Lastname = user.Lastname,
                PhoneNumber = user.PhoneNumber
            };
        }

        public static UserGetModel ToUserGetModel(this User user)
        {
            return new UserGetModel()
            {
                Id = user.Id,
                Email = user.Email,
                Nickname = user.Nickname,
                Firstname = user.Firstname,
                Lastname = user.Lastname,
                PhoneNumber = user.PhoneNumber
            };
        }

        public static UserShortModel ToUserShortModel (this UserFriend userFriend)
        {
            return new UserShortModel()
            {
                Id = userFriend.Friend.Id,
                Nickname = userFriend.Friend.Nickname,
                Firstname = userFriend.Friend.Firstname,
                Lastname = userFriend.Friend.Lastname
            };
        }

        public static UserShortModel ToUserShortModel (this User user)
        {
            return new UserShortModel
            {
                Id = user.Id,
                Nickname = user.Nickname,
                Firstname = user.Firstname,
                Lastname = user.Lastname
            };
        }

        public static List<UserShortModel> ToUserShortListModel (this IEnumerable<UserFriend> userFriends)
        {
            return userFriends.Select(x => x.ToUserShortModel()).ToList();
        }

        public static List<UserShortModel> ToUserShortListModel (this IEnumerable<User> users)
        {
            return users.Select(x => x.ToUserShortModel()).ToList();
        }
    }
}
