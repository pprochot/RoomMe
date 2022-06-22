using NUnit.Framework;
using RoomMe.SQLContext;
using RoomMe.SQLContext.Models;
using RoomMe.API.Models;
using BCryptNet = BCrypt.Net;
using RoomMe.API.Converters;
using Moq;
using System;
using Microsoft.Extensions.Logging;
using RoomMe.API.Controllers;
using RoomMe.API.Helpers;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.InMemory;
using System.Collections.Generic;
using Microsoft.Extensions.Logging.Abstractions;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Options;
using RoomMe.API.Authorization;
using System.Linq;

namespace RoomMe.API.Tests
{
    class ShoppingsListsControllerTests
    {

        private static DbContextOptions<SqlContext> options = new DbContextOptionsBuilder<SqlContext>()
            .UseInMemoryDatabase(databaseName: "InMemoryDb")
            .EnableSensitiveDataLogging()
            .Options;

        SqlContext context;
        SessionHelper sessionHelper;
        ShoppingListController shoppingListController;
        StatisticsController statisticsController;

        User sessionUser;

        [OneTimeSetUp]
        public void Setup()
        {
            context = new SqlContext(options);
            context.Database.EnsureCreated();

            sessionUser = new User()
            {
                Nickname = "SessionUser",
                Email = "Mail1",
                Password = BCryptNet.BCrypt.EnhancedHashPassword("pass123"),
                Firstname = "somename",
                Lastname = "lastname",
                PhoneNumber = "123456789",
                Friends = new List<UserFriend>()
            };

            SeedDatabase();

            var mockHttpContextAccesor = new Mock<IHttpContextAccessor>();
            var httpContext = new DefaultHttpContext();

            mockHttpContextAccesor.Setup(_ => _.HttpContext.Items["User"])
                .Returns(sessionUser);

            var mockHeaderConfiguration = new Mock<IConfiguration>();

            sessionHelper = new SessionHelper(mockHttpContextAccesor.Object, mockHeaderConfiguration.Object);

            shoppingListController = new ShoppingListController(new NullLogger<ShoppingListController>(), context, sessionHelper);
            statisticsController = new StatisticsController(new NullLogger<StatisticsController>(), context, sessionHelper);
        }

        //CreateNewShoppingLists Tests
        [Test, Order(1)]
        public async Task CreateNewShoppingList_InvalidFlatId_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.CreateNewShoppingList(new ShoppingListPostModel()
            {
                FlatId = 100
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(1)]
        public async Task CreateNewShoppingList_SessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.CreateNewShoppingList(new ShoppingListPostModel()
            {
                FlatId = 2
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(1)]
        public async Task CreateNewShoppingList_ValidShoppingList_ShouldReturnShoppingListPostReturnModel()
        {
            var actionResult = await shoppingListController.CreateNewShoppingList(new ShoppingListPostModel()
            {
                FlatId = 1,
                Name = "TestList2",
                Description = "Desc2"
            });

            Assert.IsInstanceOf<ShoppingListPostReturnModel>(actionResult.Value);
            Assert.AreEqual(2, actionResult.Value.Id);

            actionResult = await shoppingListController.CreateNewShoppingList(new ShoppingListPostModel()
            {
                FlatId = 1,
                Name = "TestList3",
                Description = "Desc3"
            });

            Assert.AreEqual(3, actionResult.Value.Id);
        }

        //GetShoppingsList tests
        [Test, Order(2)]
        public async Task GetShoppingList_InvalidListId_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.GetShoppingList(100);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(2)]
        public async Task GetShoppingList_SessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.GetShoppingList(1);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(2)]
        public async Task GetShoppingList_ValidId_ShouldReturnShoppingListGetModel()
        {
            var actionResult = await shoppingListController.GetShoppingList(2);

            Assert.IsInstanceOf<ShoppingListGetModel>(actionResult.Value);
            Assert.AreEqual(2, actionResult.Value.Id);
            Assert.AreEqual("TestList2", actionResult.Value.Name);
        }

        //AddShoppingListProducts
        [Test, Order(2)]
        public async Task AddShoppingListProducts_InvalidShoppingListId_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.AddShoppingListProducts(100, new List<ProductPostModel>());

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(2)]
        public async Task AddShoppingListProducts_SessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.AddShoppingListProducts(1, new List<ProductPostModel>());

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(2)]
        public async Task AddShoppingListProducts_ValidProductsList_ShouldReturnProductListPostReturnModel()
        {
            var actionResult = await shoppingListController.AddShoppingListProducts(2, new List<ProductPostModel>()
            {
                new ProductPostModel()
                {
                    Name = "Product1",
                    Description = "Desc1",
                    Quantity = 1,
                    Reason = "Reason1"
                },
                new ProductPostModel()
                {
                    Name = "Product2",
                    Description = "Desc2",
                    Quantity = 1,
                    Reason = "Reason2"
                },
                new ProductPostModel()
                {
                    Name = "Product3",
                    Description = "Desc3",
                    Quantity = 10,
                    Reason = "Reason3"
                },
            });

            Assert.IsInstanceOf<ProductListPostReturnModel>(actionResult.Value);
            Assert.AreEqual(3, actionResult.Value.ProductIds.Count());
        }

        [Test, Order(3)]
        public async Task GetShoppingList_ValidListAfterAddingProducts_ShouldReturnListWithThreeProducts()
        {
            var actionResult = await shoppingListController.GetShoppingList(2);

            Assert.IsInstanceOf<ShoppingListGetModel>(actionResult.Value);
            Assert.AreEqual(3, actionResult.Value.Products.Count());
            Assert.AreEqual("Product1", actionResult.Value.Products[0].Name);
            Assert.AreEqual("Product2", actionResult.Value.Products[1].Name);
            Assert.AreEqual("Product3", actionResult.Value.Products[2].Name);
        }

        //SetProductsAsBought
        [Test, Order(4)]
        public async Task SetProductsAsBought_InvalidListId_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.SetProductsAsBought(100, new List<ProductPatchModel>());

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(4)]
        public async Task SetProductsAsBought_SessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.SetProductsAsBought(1, new List<ProductPatchModel>());

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(4)]
        public async Task SetProductsAsBought_InvalidProductId_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.SetProductsAsBought(2, new List<ProductPatchModel>()
            { 
                new ProductPatchModel()
                {
                    Id = 100
                } 
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(4)]
        public async Task SetProductsAsBought_ValidProductId_ShouldReturnProductPatchReturnModel()
        {
            var actionResult = await shoppingListController.SetProductsAsBought(2, new List<ProductPatchModel>()
            {
                new ProductPatchModel()
                {
                    Id = 1,
                    Value = 123
                },
                new ProductPatchModel()
                {
                    Id = 2,
                    Value = 321
                }
            });

            Assert.IsInstanceOf<List<ProductModel>>(actionResult.Value);
            Assert.AreEqual(2, actionResult.Value.Count());

            var actionResult2 = await shoppingListController.GetShoppingList(2);

            Assert.AreEqual(true, actionResult2.Value.Products[0].Bought);
            Assert.AreEqual(true, actionResult2.Value.Products[1].Bought);
            Assert.AreEqual(false, actionResult2.Value.Products[2].Bought);
        }

        [Test, Order(5)]
        public async Task SetProductsAsBought_ProductAlreadyBought_ShouldReturnBadRequestReult()
        {
            var actionResult = await shoppingListController.SetProductsAsBought(2, new List<ProductPatchModel>()
            {
                new ProductPatchModel()
                {
                    Id = 1
                }
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(6)]
        public async Task GetCommonCostsStatistics_AllStatsId_ShouldReturnStatistics()
        {
            var actionResult = await statisticsController.GetCommonCostsStatistics(1, new StatisticsGetModel()
            {
                From = DateTime.UtcNow.AddMonths(-1),
                To = DateTime.UtcNow.AddMonths(1),
                frequencyId = Consts.AllStatsId
            });

            Assert.IsInstanceOf<IEnumerable<StatisticsReturnModel>>(actionResult.Value);

            int count = 0;

            foreach(var x in actionResult.Value)
            {
                count++;
            }

            Assert.AreEqual(2, count);
        }

        [Test, Order(6)]
        public async Task GetCommonCostsStatistics_DailyStatsId_ShouldReturnStatistics()
        {
            var actionResult = await statisticsController.GetCommonCostsStatistics(1, new StatisticsGetModel()
            {
                From = DateTime.UtcNow.AddMonths(-1),
                To = DateTime.UtcNow,
                frequencyId = Consts.DailyStatsId
            });

            Assert.IsInstanceOf<IEnumerable<StatisticsReturnModel>>(actionResult.Value);

            double sum = 0;

            foreach (var x in actionResult.Value)
            {
                sum += x.Value;
            }

            Assert.AreEqual(444, sum);
        }


        //SetShoppingListAsCompleted Tests
        [Test, Order(5)]
        public async Task SetShoppingListAsCompleted_InvalidShoppingListId_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.SetShoppingListAsCompleted(100, new List<IFormFile>());

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(5)]
        public async Task SetShoppingListAsCompleted_SessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.SetShoppingListAsCompleted(1, new List<IFormFile>());

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(5)]
        public async Task SetShoppingListAsCompleted_SomeProductsAreNotBought_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.SetShoppingListAsCompleted(2, new List<IFormFile>());

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        //RemoveProductsFromShoppingList Tests
        [Test, Order(6)]
        public async Task RemoveProductsFromShoppingList_InvalidShoppingListId_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.RemoveProductsFromShoppingList(100, new List<int>());

            Assert.IsInstanceOf<BadRequestResult>(actionResult);
        }

        [Test, Order(6)]
        public async Task RemoveProductsFromShoppingList_SessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await shoppingListController.RemoveProductsFromShoppingList(1, new List<int>());

            Assert.IsInstanceOf<BadRequestResult>(actionResult);
        }

        [Test, Order(6)]
        public async Task RemoveProductsFromShoppingList_InvalidProductId_ShouldNotDeleteAnyProducts()
        {

            var actionResult3 = await shoppingListController.GetShoppingList(2);

            Assert.AreEqual(3, actionResult3.Value.Products.Count);

            var actionResult = await shoppingListController.RemoveProductsFromShoppingList(2, new List<int>() { 99 });

            Assert.IsInstanceOf<OkResult>(actionResult);

            var actionResult2 = await shoppingListController.GetShoppingList(2);

            Assert.IsInstanceOf<ShoppingListGetModel>(actionResult2.Value);
            Assert.AreEqual(3, actionResult2.Value.Products.Count);
        }

        [Test, Order(7)]
        public async Task RemoveProductsFromShoppingList_ValidProductId_ShouldDeleteOneProduct()
        {
            var actionResult = await shoppingListController.RemoveProductsFromShoppingList(2, new List<int>() { 3 });

            Assert.IsInstanceOf<OkResult>(actionResult);

            var actionResult2 = await shoppingListController.GetShoppingList(2);

            Assert.IsInstanceOf<ShoppingListGetModel>(actionResult2.Value);
            Assert.AreEqual(2, actionResult2.Value.Products.Count);
        }

        [OneTimeTearDown]
        public void CleanUp()
        {
            context.Database.EnsureDeleted();
        }

        private void SeedDatabase()
        {
            //Add database content for testing
            context.Users.Add(sessionUser);

            var flat = new Flat()
            {
                Name = "TestFlat1",
                Address = "Address1",
                CreatorId = 1,
                Users = new List<User> { sessionUser }
            };
            context.Flats.Add(flat);


            flat = new Flat()
            {
                Name = "TestFlat2",
                Address = "Address2",
                CreatorId = 100,
                Users = new List<User> {  }
            };
            context.Flats.Add(flat);

            var list = new ShoppingList()
            {
                FlatId = 2,
                Name = "TestList1",
                Description = "SessionUser not in flat"
            };
            context.ShoppingLists.Add(list);

            context.SaveChanges();
        }
    }
}
