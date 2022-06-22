using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Models
{
    public class FlatUsersGetReturnModel
    {
        public UserNicknameModel Creator { get; set; }
        public List<UserNicknameModel> Users { get; set; }
    }
}
