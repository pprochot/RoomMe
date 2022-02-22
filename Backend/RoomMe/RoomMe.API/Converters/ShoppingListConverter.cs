﻿using RoomMe.API.Models;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.Linq;

namespace RoomMe.API.Converters
{
    public static class ShoppingListConverter
    {
        public static CommonCostModel ToCommonCostModel(this CommonCost cost)
        {
            return new CommonCostModel()
            {
                Id = cost.Id,
                UserId = cost.UserId,
                UserName = cost.User.Nickname,
                Value = cost.Value,
                Description = cost.Description,
                Date = cost.Date
            };
        }

        public static Product ToProduct(this ProductPostModel product, int authorId)
        {
            return new Product()
            {
                //TODO: this value should be received from JWT token
                AuthorId = 1,
                Name = product.Name,
                CommonCostId = null,
                Description = product.Description,
                Reason = product.Reason,
                Quantity = product.Quantity,
                Bought = false
            };
        }

        public static ProductModel ToProductModel(this Product product)
        {
            return new ProductModel()
            {
                Id = product.Id,
                AuthorId = product.AuthorId,
                AuthorName = product.Author.Nickname,
                Cost = product.CommonCost?.ToCommonCostModel(),
                Name = product.Name,
                Description = product.Description,
                Reason = product.Reason,
                Quantity = product.Quantity,
                Bought = product.Bought
            };
        }

        public static ProductListPostReturnModel ToProductListPostReturnModel(this IEnumerable<Product> products)
        {
            return new ProductListPostReturnModel()
            {
                ProductIds = products.Select(x => x.Id).ToList(),
                CreationDate = DateTime.Now
            };
        }

        public static ProductPatchReturnModel ToProductPatchReturnModel(this IEnumerable<Product> products)
        {
            return new ProductPatchReturnModel()
            {
                TimeStamp = DateTime.Now,
                CommonCostIds = products.Select(x => x.Id).ToList()
            };
        }

        public static CommonCost CreateCommonCost(this ProductPatchModel product, int flatId, int userId)
        {
            return new CommonCost()
            {
                FlatId = flatId,
                UserId = userId,
                Value = product.Value,
                Description = product.Description,
                IsDivided = product.IsDivided,
                Date = DateTime.Now
            };
        }

        public static void SetToBoughtState(this Product entity, ProductPatchModel product, int flatId, int userId)
        {
            entity.Bought = true;
            entity.CommonCost = product.CreateCommonCost(flatId, userId);
        }

        public static ShoppingList ToShoppingList(this ShoppingListPostModel list, int flatId)
        {
            return new ShoppingList()
            {
                FlatId = flatId,
                Name = list.Name,
                Description = list.Description,
                CreationDate = DateTime.Now,
            };
        }

        public static ShoppingListPostReturnModel ToShoppingListPostReturnModel(this ShoppingList list)
        {
            return new ShoppingListPostReturnModel()
            {
                Id = list.Id,
                CreationDate = DateTime.Now
            };
        }

        public static ShoppingListGetModel ToShoppingListGetModel(this ShoppingList list)
        {
            return new ShoppingListGetModel()
            {
                Id = list.FlatId,
                FlatId = list.FlatId,
                CompletorId = list.CompletorId,
                CompletorName = list.Completor?.Nickname,
                Name = list.Name,
                Description = list.Description,
                CreationDate = list.CreationDate,
                CompletionDate = list.CompletionDate,
                Products = list.Products.Select(x => x.ToProductModel()).ToList()
            };
        }

        public static Receipt ToReceipt(this ReceiptFileModel fileModel, int listId, string path, Guid guid)
        {
            return new Receipt()
            {
                ShoppingListId = listId,
                Path = path,
                Name = fileModel.fileName,
                Extension = fileModel.Extension,
                Guid = guid
            };
        }
    }
}