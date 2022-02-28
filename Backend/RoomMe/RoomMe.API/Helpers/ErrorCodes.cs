using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Helpers
{
    public static class ErrorCodes
    {
        public enum SignUpErrors
        {
            EmailAlreadyInDB
        };

        public enum SignInErrors
        {
            WrongEmailOrPassword
        };
    }
}
