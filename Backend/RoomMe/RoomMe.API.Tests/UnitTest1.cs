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
    public class Tests
    {

        private static DbContextOptions<SqlContext> options = new DbContextOptionsBuilder<SqlContext>()
            .UseInMemoryDatabase(databaseName: "InMemoryDb")
            .Options;

        SqlContext context;
        SessionHelper sessionHelper;
        FlatController flatController;
        //AuthController authController;


        [OneTimeSetUp]
        public void Setup()
        {
            context = new SqlContext(options);
            context.Database.EnsureCreated();

            SeedDatabase();

            //Mock IHttpContextAccessor
            var mockHttpContextAccesor = new Mock<IHttpContextAccessor>();
            var httpContext = new DefaultHttpContext();
            //var fakeToken = "FakeToken";
            //httpContext.Request.Headers["Authorization"] = fakeToken;

            //mockHttpContextAccesor.Setup(_ => _.HttpContext).Returns(httpContext);
            mockHttpContextAccesor.Setup(_ => _.HttpContext.Items["User"])
                .Returns(new User()
                {
                    Id = 1,
                });

            //Mock HeaderConfiguration
            var mockHeaderConfiguration = new Mock<IConfiguration>();

            sessionHelper = new SessionHelper(mockHttpContextAccesor.Object, mockHeaderConfiguration.Object);
            
            flatController = new FlatController(new NullLogger<FlatController>(), context, sessionHelper);
        }

        [Test]
        public void Test()
        {
            Assert.Pass();
        }

        [Test]
        public async Task InMemoryDbTest()
        {
            var user = await context.Users.FirstOrDefaultAsync(x => x.Id == 1).ConfigureAwait(false);

            var expectedUser = new User()
            {
                Id = 1,
                Nickname = "TestUser1",
                Email = "Mail1",
                Password = user.Password,
                Firstname = "somename",
                Lastname = "lastname",
                PhoneNumber = "123456789"
            };

            Assert.IsInstanceOf<User>(user);
        }

        [Test]
        public async Task GetFlat_WithExistingId_ShouldReturnFlatGetModel()
        {
            var actionResult = await flatController.GetFlat(1);

            Assert.IsInstanceOf<FlatGetModel>(actionResult.Value);
            Assert.AreEqual(1, actionResult.Value.Id);
        }

        [OneTimeTearDown]
        public void CleanUp()
        {
            context.Database.EnsureDeleted();
        }

        private void SeedDatabase()
        {
            //Add database content for testing

            var user = new User()
            {
                Id = 1,
                Nickname = "TestUser1",
                Email = "Mail1",
                Password = BCryptNet.BCrypt.EnhancedHashPassword("pass123"),
                Firstname = "somename",
                Lastname = "lastname",
                PhoneNumber = "123456789"
            };

            context.Users.Add(user);
            //AuthController SignIn

            var flat = new Flat()
            {
                Id = 1,
                Name = "TestFlat1",
                Address = "SomeAddress1",
                CreatorId = 1,
                Users = new List<User> { user}
            };

            context.Flats.Add(flat);

            context.SaveChanges();
        }

    }
}