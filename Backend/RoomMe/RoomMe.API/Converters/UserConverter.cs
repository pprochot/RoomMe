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

        public static UserGetModel ToUserGetModel(this User user)
        {
            return new UserGetModel()
            {
                Id = user.Id,
                Email = user.Email,
                Firstname = user.Firstname,
                Lastname = user.Lastname,
                PhoneNumber = user.PhoneNumber
            };
        }

        public static UserFriendModel ToUserFriendModel (this UserFriend userFriend)
        {

            return new UserFriendModel()
            {
                Id = userFriend.Friend.Id,
                Nickname = userFriend.Friend.Nickname,
                Firstname = userFriend.Friend.Firstname,
                Lastname = userFriend.Friend.Lastname
            };
        }

        public static List<UserFriendModel> ToUserFriendListModel (this IEnumerable<UserFriend> userFriends)
        {
            return userFriends.Select(x => x.ToUserFriendModel()).ToList();
        }
    }
}
