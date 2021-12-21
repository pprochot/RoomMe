using RoomMe.API.Models;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Converters
{
    public static class RentCostConverter
    {
        public static RentCost ToRentCost(this RentCostPutModel cost, int flatId, int userId)
        {
            return new RentCost()
            {
                FlatId = flatId,
                UserId = userId,
                Value = cost.Value,
                CreationDate = DateTime.Now
            };
        }

        public static RentCostPostReturnModel ToRentCostPostReturnModel(this RentCost cost)
        {
            return new RentCostPostReturnModel()
            {
                userId = cost.UserId,
                flatId = cost.FlatId,
                CreationDate = DateTime.Now
            };
        }

        public static void UpdateRentCost(this RentCost entityCost, RentCostPutModel cost)
        {
            entityCost.Value = cost.Value;
            entityCost.CreationDate = DateTime.Now;
        }
    }
}
