using RoomMe.API.Helpers;
using RoomMe.API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Validators
{
    public static class SignUpValidator
    {
        public static bool IsValid(this SignUpUserModel model)
        {
            if(model.Nickname.Length < Consts.MinNickLength || model.Nickname.Length > Consts.MaxNickLength)
            {
                return false;
            }

            if(model.Password.Length < Consts.MinPasswordLength || model.Password.Length > Consts.MaxPasswordLength)
            {
                return false;
            }

            return true;
        }
    }
}
