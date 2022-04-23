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
    class UserControllerTests
    {

        private static DbContextOptions<SqlContext> options = new DbContextOptionsBuilder<SqlContext>()
            .UseInMemoryDatabase(databaseName: "InMemoryDb")
            .EnableSensitiveDataLogging()
            .Options;

        SqlContext context;
        SessionHelper sessionHelper;
        UserController userController;

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

            userController = new UserController(new NullLogger<UserController>(), context, sessionHelper);
        }

        //GetUserInfo Tests
        [Test, Order(1)]
        public async Task GetUserInfo_ShouldReturnUserGetModel()
        {
            var actionResult = await userController.GetUserInfo();

            Assert.IsInstanceOf<UserGetModel>(actionResult.Value);
            Assert.AreEqual(sessionUser.Nickname, actionResult.Value.Nickname);
            Assert.AreEqual(sessionUser.Id, actionResult.Value.Id);
            Assert.AreEqual(sessionUser.Firstname, actionResult.Value.Firstname);
        }

        //GetFlats Tests
        [Test, Order(1)]
        public async Task GetFlats_ShouldReturnFlatNameModelList()
        {
            var actionResult = await userController.GetFlats();

            Assert.IsInstanceOf<List<FlatShortModel>>(actionResult.Value);
            Assert.AreEqual(2, actionResult.Value.Count());
        }

        //GetUsers Tests
        [Test, Order(1)]
        public async Task GetUsers_WithInvalidPhrase_ShouldReturnEmptyUserShortListModel()
        {
            var actionResult = await userController.GetUsers("wrong");

            Assert.IsInstanceOf<List<UserShortModel>>(actionResult);
            Assert.AreEqual(0, actionResult.Count());
        }

        [Test, Order(1)]
        public async Task GetUsers_WithValidPhrase_ShouldReturnUserShortListModel()
        {
            var actionResult = await userController.GetUsers("User");

            Assert.IsInstanceOf<List<UserShortModel>>(actionResult);
            Assert.AreEqual(2, actionResult.Count());
        }
      
        //AddFriends Tests
        [Test, Order(1)]
        public async Task AddFriend_InvalidFriendID_ShouldReturnBadRequestResult()
        {
            var actionResult = await userController.AddFriend(100);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }
        
        [Test, Order(2)]
        public async Task AddFriend_ValidFriend_ShouldReturnDateTime()
        {
            var actionResult = await userController.AddFriend(2);

            Assert.IsInstanceOf<DateTime>(actionResult.Value);
        }

        [Test, Order(3)]
        public async Task AddFriend_FriendAlreadyAdded_ShouldReturnBadRequestResult()
        {
            var actionResult = await userController.AddFriend(2);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        //GetFriends Tests
        [Test, Order(3)]
        public async Task GetFriends_SessionUserExists_ShouldReturnListUserShortModel()
        {
            var actionResult = await userController.GetFriends();

            Assert.IsInstanceOf<List<UserShortModel>>(actionResult.Value);
            Assert.AreEqual(1, actionResult.Value.Count());
            Assert.AreEqual(2, actionResult.Value.First().Id);
        }

        //DeleteFriends Tests
        [Test, Order(4)]
        public async Task DeleteFriend_WrongFriendId_ShouldReturnBadRequestResult()
        {
            var actionResult = await userController.DeleteFriend(100);

            Assert.IsInstanceOf<BadRequestResult>(actionResult);
        }

        [Test,Order(4)]
        public async Task DeleteFriend_ValidFriendId_ShouldReturnOkResult()
        {
            var actionResult = await userController.DeleteFriend(2);

            Assert.IsInstanceOf<OkResult>(actionResult);
        }
        [Test,Order(5)]
        public async Task GetFriends_ShouldReturnEmptyList()
        {
            var actionResult = await userController.GetFriends();

            Assert.IsInstanceOf<List<UserShortModel>>(actionResult.Value);
            Assert.AreEqual(0, actionResult.Value.Count());
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
                Users = new List<User> { sessionUser}
            };
            context.Flats.Add(flat);

            var user = new User()
            {
                Nickname = "Test1",
                Firstname = "UserName1",
                Lastname = "Last1",
                Password = BCryptNet.BCrypt.EnhancedHashPassword("pass123"),
                Friends = new List<UserFriend>()
            };
            context.Users.Add(user);

            flat = new Flat()
            {
                Name = "TestFlat2",
                Address = "Address2",
                CreatorId = 2,
                Users = new List<User> { sessionUser }
            };
            context.Flats.Add(flat);

            user = new User()
            {
                Nickname = "Test2",
                Firstname = "Name2",
                Lastname = "UserLastname2",
                Password = BCryptNet.BCrypt.EnhancedHashPassword("pass123"),
                Friends = new List<UserFriend>()
            };
            context.Users.Add(user);

            context.SaveChanges();
        }
    }
}
