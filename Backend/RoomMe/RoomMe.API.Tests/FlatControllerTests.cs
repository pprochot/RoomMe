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

namespace RoomMe.API.Tests
{
    public class FlatControllerTests
    {
        private static DbContextOptions<SqlContext> options = new DbContextOptionsBuilder<SqlContext>()
            .UseInMemoryDatabase(databaseName: "InMemoryDb")
            .EnableSensitiveDataLogging()
            .Options;

        SqlContext context;
        SessionHelper sessionHelper;
        FlatController flatController;
        StatisticsController statisticsController;

        User sessionUser;

        [OneTimeSetUp]
        public void Setup()
        {
            context = new SqlContext(options);
            context.Database.EnsureCreated();

            sessionUser = new User()
            {
                Nickname = "TestUser1",
                Email = "Mail1",
                Password = BCryptNet.BCrypt.EnhancedHashPassword("pass123"),
                Firstname = "somename",
                Lastname = "lastname",
                PhoneNumber = "123456789",
                Friends = new List<UserFriend>() { new UserFriend()
                {
                    FriendId = 2,
                    UserId = 1
                } }
            };

            SeedDatabase();

            var mockHttpContextAccesor = new Mock<IHttpContextAccessor>();
            var httpContext = new DefaultHttpContext();

            mockHttpContextAccesor.Setup(_ => _.HttpContext.Items["User"])
                .Returns(sessionUser);

            var mockHeaderConfiguration = new Mock<IConfiguration>();

            sessionHelper = new SessionHelper(mockHttpContextAccesor.Object, mockHeaderConfiguration.Object);
            
            flatController = new FlatController(new NullLogger<FlatController>(), context, sessionHelper);
            statisticsController = new StatisticsController(new NullLogger<StatisticsController>(), context, sessionHelper);
        }
        
        //CreateNewFlat Test
        [Test, Order(1)]
        public async Task CreateNewFlat_WithUnexistingUser_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.CreateNewFlat(new FlatPostModel()
            {
                Name = "AddTestFlat",
                Address = "Address",
                Users = new List<int> { 100}
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(2)]
        public async Task CreateNewFlat_WithValidPostModel_ShouldReturnFlatPostReturnModel()
        {
            var actionResult = await flatController.CreateNewFlat(new FlatPostModel()
            {
                Name = "AddTestFlat3",
                Address = "Address3",
                Users = new List<int>()
            });

            Assert.IsInstanceOf<FlatPostReturnModel>(actionResult.Value);
            Assert.AreEqual(3, actionResult.Value.Id);
        }


        //GetFlat Tests
        [Test, Order(3)]
        public async Task GetFlat_WithExistingId_ShouldReturnFlatGetModel()
        {
            var actionResult = await flatController.GetFlat(3);

            Assert.IsInstanceOf<FlatGetModel>(actionResult.Value);
            Assert.AreEqual(3, actionResult.Value.Id);
        }

        [Test, Order(3)]
        public async Task GetFlat_WithWrongId_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.GetFlat(100);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(3)]
        public async Task GetFlat_WithUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.GetFlat(1);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        //AddUserToFlat Tests
        [Test, Order(4)]
        public async Task AddUserToFlat_WithWrongFlatId_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.AddUserToFlat(100, 1);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }
        [Test, Order(4)]
        public async Task AddUserToFlat_WithCreatorNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.AddUserToFlat(1, 1);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }
        [Test, Order(4)]
        public async Task AddUserToFlat_WithUserAlreadyAdded_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.AddUserToFlat(3, 1);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }
        [Test, Order(4)]
        public async Task AddUserToFlat_WithWrongUserId_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.AddUserToFlat(3, 100);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }
        [Test, Order(5)]
        public async Task AddUserToFlat_WithValidIds_ShouldReturnUserShortModel()
        {
            var actionResult = await flatController.AddUserToFlat(3, 2);

            Assert.IsInstanceOf<UserShortModel>(actionResult.Value);
            Assert.AreEqual(2, actionResult.Value.Id);
            Assert.AreEqual("TestUser2", actionResult.Value.Nickname);
        }

        //GetFlatUsers Tests
        [Test, Order(6)]
        public async Task GetFlatUsers_WithExistingId_ShouldReturnFlatUsersGetReturnModel()
        {
            var actionResult = await flatController.GetFlatUsers(3);

            Assert.IsInstanceOf<FlatUsersGetReturnModel>(actionResult.Value);
            Assert.AreEqual(1, actionResult.Value.Creator.Id);
            Assert.AreEqual(2, actionResult.Value.Users[0].Id);
        }

        [Test, Order(6)]
        public async Task GetFlatUsers_WithUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.GetFlatUsers(1);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(6)]
        public async Task GetFlatUsers_WithWrongFlatId_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.GetFlatUsers(100);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        //RemoveUserFromFlat Tests
        [Test, Order(7)]
        public async Task RemoveUserFromFlat_WithWrongFlatId_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.RemoveUserFromFlat(100, 2);

            Assert.IsInstanceOf<BadRequestResult>(actionResult);
        }
        [Test, Order(7)]
        public async Task RemoveUserFromFlat_WithCreatorNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.RemoveUserFromFlat(1, 2);

            Assert.IsInstanceOf<BadRequestResult>(actionResult);
        }
        [Test, Order(7)]
        public async Task RemoveUserFromFlat_WithWrongUserId_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.RemoveUserFromFlat(3, 100);

            Assert.IsInstanceOf<BadRequestResult>(actionResult);
        }
        [Test, Order(7)]
        public async Task RemoveUserFromFlat_WithValidUserIdButNotForThisFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.RemoveUserFromFlat(3, 3);

            Assert.IsInstanceOf<BadRequestResult>(actionResult);
        }
        [Test, Order(8)]
        public async Task RemoveUserFromFlat_WithValidUserId_ShouldReturnOkResult()
        {
            var actionResult = await flatController.RemoveUserFromFlat(3, 2);

            Assert.IsInstanceOf<OkResult>(actionResult);
        }
        
        //tRentCost Tests
        [Test, Order(8)]
        public async Task PostRentCost_RentCostNotYetAdded_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.PostRentCost(3);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(8)]
        public async Task CheckIfRentIsPaid_RentNotYetAdded_ShouldReturnIsPaidFalse()
        {
            var actionResult = await flatController.CheckIfRentIsPaid(3);

            Assert.IsInstanceOf<RentCostGetReturnModel>(actionResult.Value);
            Assert.AreEqual(false, actionResult.Value.IsPaid);
        }

        [Test, Order(9)]
        public async Task SetFlatRentCost_WithWrongFlatId_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.SetFlatRentCost(100, new RentCostPutModel());

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }
        [Test, Order(9)]
        public async Task SetFlatRentCost_WithSessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.SetFlatRentCost(1, new RentCostPutModel());

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }
        [Test, Order(9)]
        public async Task SetFlatRentCost_WithNoCostAlreadyAdded_ShouldReturnRentCostPostReturnModel()
        {
            var actionResult = await flatController.SetFlatRentCost(3, new RentCostPutModel()
            {
                Value = 100.0
            });

            Assert.IsInstanceOf<RentCostPostReturnModel>(actionResult.Value);
            Assert.AreEqual(1, actionResult.Value.UserId);
        }
        [Test, Order(10)]
        public async Task SetFlatRentCost_WithCostAlreadyAdded_ShouldReturnRentCostPostReturnModel()
        {
            var actionResult = await flatController.SetFlatRentCost(3, new RentCostPutModel()
            {
                Value = 150.0
            });

            Assert.IsInstanceOf<RentCostPostReturnModel>(actionResult.Value);
        }

        [Test, Order(11)]
        public async Task PostRentCost_InvalidFlatIdOrSessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.PostRentCost(99);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);

            actionResult = await flatController.PostRentCost(1);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(11)]
        public async Task PostRentCost_Valid_ShouldReturnPrivateCostModel()
        {
            var actionResult = await flatController.PostRentCost(3);

            Assert.IsInstanceOf<PrivateCostModel>(actionResult.Value);
            Assert.AreEqual(150.0, actionResult.Value.Value);
        }

        [Test, Order(12)]
        public async Task GetPrivateCostsStatistics_AllStatsId_ShouldReturnPrivateStats()
        {
            var actionResult = await statisticsController.GetPrivateCostsStatistics(new StatisticsGetModel()
            {
                From = DateTime.UtcNow.AddMonths(-1),
                To = DateTime.UtcNow,
                frequencyId = Consts.AllStatsId
            });

            Assert.IsInstanceOf<IEnumerable<StatisticsReturnModel>>(actionResult.Value);

            double sum = 0;

            foreach (var x in actionResult.Value)
            {
                sum += x.Value;
            }

            Assert.AreEqual(150, sum);
        }

        [Test, Order(13)]
        public async Task GetPrivateCostsStatistics_AllStatsIdWithTwoRents_ShouldReturnPrivateStats()
        {
            await flatController.SetFlatRentCost(2, new RentCostPutModel()
            {
                Value = 150
            });

            await flatController.PostRentCost(2);

            var actionResult = await statisticsController.GetPrivateCostsStatistics(new StatisticsGetModel()
            {
                From = DateTime.UtcNow.AddMonths(-1),
                To = DateTime.UtcNow,
                frequencyId = Consts.AllStatsId
            });

            Assert.IsInstanceOf<IEnumerable<StatisticsReturnModel>>(actionResult.Value);

            int count = 0;
            double sum = 0;

            foreach(var x in actionResult.Value)
            {
                sum += x.Value;
                count++;
            }

            Assert.AreEqual(2, count);
            Assert.AreEqual(300, sum);
        }

        [Test, Order(14)]
        public async Task GetPrivateCostsStatistics_DailydWithTwoRents_ShouldReturnPrivateStats()
        {
            var actionResult = await statisticsController.GetPrivateCostsStatistics(new StatisticsGetModel()
            {
                From = DateTime.UtcNow.AddMonths(-1),
                To = DateTime.UtcNow,
                frequencyId = Consts.DailyStatsId
            });

            Assert.IsInstanceOf<IEnumerable<StatisticsReturnModel>>(actionResult.Value);

            int count = 0;
            double sum = 0;

            foreach (var x in actionResult.Value)
            {
                sum += x.Value;
                count++;
            }

            Assert.AreEqual((DateTime.UtcNow.Date - DateTime.UtcNow.AddMonths(-1).Date).TotalDays, count);
            Assert.AreEqual(300, sum);
        }

        [Test, Order(12)]
        public async Task CheckIfRentIsPaid_RentCostAdded_ShouldReturnIsPaidTrue()
        {
            var actionResult = await flatController.CheckIfRentIsPaid(3);

            Assert.IsInstanceOf<RentCostGetReturnModel>(actionResult.Value);
            Assert.AreEqual(true, actionResult.Value.IsPaid);
        }

        [Test, Order(12)]
        public async Task GetAvailableLocators_SessionUserAloneInFlat_ShouldReturnOneUsersNicknameModels()
        {
            var actionResult = await flatController.GetAvailableLocators(3);

            Assert.IsInstanceOf<IEnumerable<UserNicknameModel>>(actionResult.Value);

            int count = 0;
            foreach(var x in actionResult.Value)
                count++;

            Assert.AreEqual(1, count);
        }

        //GetFlatShoppingLists Tests
        [Test, Order(12)]
        public async Task GetFlatShoppingLists_WithNoListsForFlat_ShouldReturnEmptyList()
        {
            var actionResult = await flatController.GetFlatShoppingLists(3);

            Assert.IsInstanceOf<List<ShoppingListShortModel>>(actionResult.Value);
            Assert.AreEqual("", actionResult.Value);
        }
        [Test, Order(12)]
        public async Task GetFlatShoppingLists_WithSessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await flatController.GetFlatShoppingLists(1);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }
        [Test, Order(12)]
        public async Task GetFlatShoppingLists_WithValidUserAndList_ShouldReturnListWithContent()
        {
            var actionResult = await flatController.GetFlatShoppingLists(2);

            Assert.IsInstanceOf<List<ShoppingListShortModel>>(actionResult.Value);
            Assert.AreNotEqual("", actionResult.Value);
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
                Address = "SomeAddress1",
                CreatorId = 2,
                Users = new List<User> {},
                ShoppingLists = new List<ShoppingList> { new ShoppingList()
                {
                    FlatId = 1,
                    Name = "ShoppingList1",
                    Description = "Desc",
                    Products = new List<Product>()
                } }
            };
            context.Flats.Add(flat);

            var user = new User()
            {
                Nickname = "TestUser2",
                Email = "Mail2",
                Password = BCryptNet.BCrypt.EnhancedHashPassword("pass123"),
                Firstname = "someone2",
                Lastname = "lastname2",
                PhoneNumber = "123456789"
            };
            context.Users.Add(user);

            flat = new Flat()
            {
                Name = "TestFlat2",
                Address = "SomeAddress2",
                CreatorId = 1,
                Users = new List<User> { sessionUser, user },
                ShoppingLists = new List<ShoppingList> { new ShoppingList()
                {
                    FlatId = 2,
                    Name = "ShoppingList1",
                    Description = "Desc",
                    Products = new List<Product>()
                } }
            };
            context.Flats.Add(flat);

            user = new User()
            {
                Nickname = "TestUser3",
                Email = "Mail3",
                Password = BCryptNet.BCrypt.EnhancedHashPassword("pass123"),
                Firstname = "someone3",
                Lastname = "lastname3",
                PhoneNumber = "123456789",
            };
            context.Users.Add(user);


            context.SaveChanges();
        }

    }
}