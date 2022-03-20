using RoomMe.API.Models;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Converters
{
    public static class FlatConverter
    {
        public static FlatGetModel ToFlatGetModel(this Flat flat)
        {
            return new FlatGetModel()
            {
                Id = flat.Id,
                Name = flat.Name,
                Address = flat.Address
            };
        }

        public static Flat ToFlatModel(this FlatPostModel flat, List<User> users, User creator)
        {
            return new Flat()
            {
                Name = flat.Name,
                Address = flat.Address,
                Users = users,
                Creator = creator
            };
        }

        public static FlatPostReturnModel ToFlatPostReturnModel(this Flat flat)
        {
            return new FlatPostReturnModel()
            {
                Id = flat.Id,
                CreationDate = DateTime.UtcNow
            };
        }

        public static FlatShortModel ToFlatNameModel(this Flat flat)
        {
            return new FlatShortModel()
            {
                Id = flat.Id,
                Name = flat.Name,
                Address = flat.Address
            };
        }

        public static List<FlatShortModel> ToFlatNameModelList(this IEnumerable<Flat> flats)
        {
            return flats.Select(x => x.ToFlatNameModel()).ToList();
        }

        public static FlatUsersGetReturnModel ToFlatUsersGetReturnModel(this Flat flat)
        {
            return new FlatUsersGetReturnModel()
            {
                Creator = flat.Creator.ToUserNicknameModel(),
                Users = flat.Users.Where(x => x.Id != flat.CreatorId).Select(x => x.ToUserNicknameModel()).ToList()
            };
        }
    }
}
