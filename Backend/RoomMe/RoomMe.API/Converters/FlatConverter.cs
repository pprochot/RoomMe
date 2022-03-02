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
        public static FlatFullGetModel ToFlatFullGetModel(this Flat flat)
        {
            return new FlatFullGetModel()
            {
                Id = flat.Id,
                Name = flat.Name,
                Address = flat.Address,
                Users = flat.Users.Select(x => x.ToUserNicknameModel()).ToList()
            };
        }

        public static Flat ToFlatModel(this FlatPostModel flat, List<User> users)
        {
            return new Flat()
            {
                Name = flat.Name,
                Address = flat.Address,
                Users = users
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

        public static FlatNameModel ToFlatNameModel(this Flat flat)
        {
            return new FlatNameModel()
            {
                Id = flat.Id,
                Name = flat.Name
            };
        }

        public static List<FlatNameModel> ToFlatNameModelList(this IEnumerable<Flat> flats)
        {
            return flats.Select(x => x.ToFlatNameModel()).ToList();
        }
    }
}
