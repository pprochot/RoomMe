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
    public class HouseworkScheduleTests
    {
        private static DbContextOptions<SqlContext> options = new DbContextOptionsBuilder<SqlContext>()
            .UseInMemoryDatabase(databaseName: "InMemoryDb")
            .EnableSensitiveDataLogging()
            .Options;

        SqlContext context;
        SessionHelper sessionHelper;
        HouseworkController houseworkController;
        ScheduleController scheduleController;

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

            houseworkController = new HouseworkController(new NullLogger<HouseworkController>(), context, sessionHelper);
            scheduleController = new ScheduleController(new NullLogger<ScheduleController>(), context, sessionHelper);
        }

        [Test, Order(1)]
        public async Task PostHousework_InvalidUserId_ShouldReturnBadRequestResult()
        {
            var actionResult = await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 1,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2, 99 },
                Days = new int[] { 1 },
                FrequencyId = 1
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(1)]
        public async Task PostHousework_InvalidFlatId_ShouldReturnBadRequestResult()
        {
            var actionResult = await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 99,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2},
                Days = new int[] { 1 },
                FrequencyId = 1
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(1)]
        public async Task PostHousework_SessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 2,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2 },
                Days = new int[] { 1 },
                FrequencyId = 1
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(1)]
        public async Task PostHousework_InvalidFrequency_ShouldReturnBadRequestResult()
        {
            var actionResult = await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 1,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2 },
                Days = new int[] { 1 },
                FrequencyId = 99
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(1)]
        public async Task PostHousework_InvalidDay_ShouldReturnBadRequestResult()
        {
            var actionResult = await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 1,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2 },
                Days = new int[] { 0 },
                FrequencyId = 1
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);

            actionResult = await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 1,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2 },
                Days = new int[] { 8 },
                FrequencyId = 1
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(1)]
        public async Task PostHousework_InvalidFrequencyDaysCombinations_ShouldReturnBadRequestResults()
        {
            var actionResult = await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 1,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2 },
                Days = new int[] { 1 , 2 },
                FrequencyId = (int)Consts.HouseworkFrequencies.Once
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);

            actionResult = await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 1,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2 },
                Days = new int[] { 1, 2, 3, 4 },
                FrequencyId = (int)Consts.HouseworkFrequencies.Daily
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);

            actionResult = await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 1,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2 },
                Days = new int[] { 1, 2, 3},
                FrequencyId = (int)Consts.HouseworkFrequencies.TwiceAWeek
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);

            actionResult = await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 1,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2 },
                Days = new int[] { 1, 2},
                FrequencyId = (int)Consts.HouseworkFrequencies.Weekly
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(2)]
        public async Task PostHousework_ValidHouseworkPostModel_ShouldReturnHouseworkPostReturnModel()
        {
            var actionResult = await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 1,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2 },
                Days = new int[] { 1 },
                FrequencyId = (int)Consts.HouseworkFrequencies.Once
            });
            
            Assert.IsInstanceOf<HouseworkPostReturnModel>(actionResult.Value);
            Assert.AreEqual(2, actionResult.Value.Id);
            Assert.AreEqual(2, actionResult.Value.SettingsId);
        }

        [Test, Order(3)]
        public async Task GetHouseworkFull_InvalidHouseworkId_ShouldReturnBadRequestResult()
        {
            var actionResult = await houseworkController.GetHouseworkFull(99);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(3)]
        public async Task GetHouseworkFull_SessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await houseworkController.GetHouseworkFull(1);

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(3)]
        public async Task GetHouseworkFull_ValidHouseworkId_ShouldReturnHouseworkModel()
        {
            var actionResult = await houseworkController.GetHouseworkFull(2);

            Assert.IsInstanceOf<HouseworkModel>(actionResult.Value);
            Assert.AreEqual("TestHousework1", actionResult.Value.Name);
            Assert.AreEqual(1, actionResult.Value.Flat.Id);
        }



        [Test, Order(4)]
        public async Task GetScheduleByMonth_InvalidFlatId_ShouldReturnBadRequestResult()
        {
            var actionResult = await scheduleController.GetSchedulesByMonth(99, new SchedulesByMonthModel()
            {
                Month = DateTime.UtcNow.Month,
                Year = DateTime.UtcNow.Year
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(4)]
        public async Task GetScheduleByMonth_SessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionResult = await scheduleController.GetSchedulesByMonth(2, new SchedulesByMonthModel()
            {
                Month = DateTime.Now.Month,
                Year = DateTime.Now.Year
            });

            Assert.IsInstanceOf<BadRequestResult>(actionResult.Result);
        }

        [Test, Order(5)]
        public async Task GetScheduleByMonth_Once_ShouldReturnOneSchedule()
        {
            var actionResult = await scheduleController.GetSchedulesByMonth(1, new SchedulesByMonthModel()
            {
                Month = DateTime.Now.Month,
                Year = DateTime.Now.Year
            });

            Assert.IsInstanceOf<Dictionary<DateTime,List<ScheduleModel>>>(actionResult.Value);
            Assert.AreEqual(DateTime.DaysInMonth(DateTime.Now.Year, DateTime.Now.Month), actionResult.Value.Count);

            int count = 0;

            foreach(var x in actionResult.Value)
            {
                count += x.Value.Count;

                if (x.Value.Count > 0)
                {
                    Assert.AreEqual((int)Consts.HouseworkStatuses.ToDo, x.Value[0].Status.Id);
                }
            }

            Assert.AreEqual(1, count);
        }

        [Test, Order(5)]
        public async Task GetScheduleByMonth_Daily_ShouldReturnRemainingDaysInMonthAmountOfSchedules()
        {
            await houseworkController.PostHousework(new HouseworkPostModel()
            {
                Name = "TestHousework1",
                FlatId = 1,
                Description = "TestDesc1",
                Users = new List<int>() { 1, 2 },
                Days = new int[] { 1, 2, 3, 4, 5, 6, 7 },
                FrequencyId = (int)Consts.HouseworkFrequencies.Daily
            });

            var actionResult = await scheduleController.GetSchedulesByMonth(1, new SchedulesByMonthModel()
            {
                Month = DateTime.UtcNow.Month,
                Year = DateTime.UtcNow.Year
            });

            Assert.IsInstanceOf<Dictionary<DateTime, List<ScheduleModel>>>(actionResult.Value);
            Assert.AreEqual(DateTime.DaysInMonth(DateTime.Now.Year, DateTime.Now.Month), actionResult.Value.Count);

            int count = 0;

            foreach (var x in actionResult.Value)
            {
                count += x.Value.Count;

                if (x.Value.Count > 0)
                {
                    Assert.AreEqual((int)Consts.HouseworkStatuses.ToDo, x.Value[0].Status.Id);
                Console.WriteLine(x.Value[0].Id);

                }
            }

            int expectedAmount = (int)(new DateTime(DateTime.UtcNow.Year, DateTime.UtcNow.AddMonths(1).Month, 1) - DateTime.UtcNow).TotalDays;

            Assert.AreEqual(expectedAmount, count);
        }

        [Test, Order(6)]
        public async Task PatchSchedule_InvalidScheduleId_ShouldReturnBadRequestResult()
        {
            var actionResult = await scheduleController.PatchSchedule(99, new SchedulePatchModel());

            Assert.IsInstanceOf<BadRequestResult>(actionResult);
        }

        [Test, Order(6)]
        public async Task PatchSchedule_ChangeStatus_ShouldChangeScheduleStatus()
        {
            var actionResult = await scheduleController.PatchSchedule(2, new SchedulePatchModel()
            {
                Date = null,
                UserId = null,
                StatusId = (int)Consts.HouseworkStatuses.Done
            });

            Assert.IsInstanceOf<OkResult>(actionResult);

            var actionResult2 = await scheduleController.GetSchedulesByMonth(1, new SchedulesByMonthModel()
            {
                Month = DateTime.UtcNow.Month,
                Year = DateTime.UtcNow.Year
            });

            int count = 0;
            foreach(var x in actionResult2.Value)
            {
                if(x.Value.Count > 0)
                if (x.Value[0].Status.Id == (int)Consts.HouseworkStatuses.Done)
                    count++;
            }

            Assert.AreEqual(1, count);
        }

        [Test, Order(1)]
        public async Task RemoveHousework_InvalidHouseworkId_ShouldReturnBadRequestResult()
        {
            var actionResult = await houseworkController.RemoveHousework(99);

            Assert.IsInstanceOf<BadRequestResult>(actionResult);
        }

        [Test, Order(1)]
        public async Task RemoveHousework_SessionUserNotOfFlat_ShouldReturnBadRequestResult()
        {
            var actionRestult = await houseworkController.RemoveHousework(1);

            Assert.IsInstanceOf<BadRequestResult>(actionRestult);
        }

        [Test, Order(7)]
        public async Task RemoveHousework_ValidHouseworkId_ShouldReturnOKResult()
        {
            var actionResult = await houseworkController.RemoveHousework(2);

            Assert.IsInstanceOf<OkResult>(actionResult);

            var actionResult2 = await houseworkController.GetHouseworkFull(2);

            Assert.IsInstanceOf<BadRequestResult>(actionResult2.Result);
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

            User user = new User()
            {
                Nickname = "TestUser1",
                Firstname = "FirstName1",
                Lastname = "LastName1"
            };
            context.Users.Add(user);

            Flat flat = new Flat()
            {
                Name = "TestFlat1",
                Address = "Address1",
                Creator = sessionUser,
                Users = new List<User>() { sessionUser, user }
            };
            context.Flats.Add(flat);

            flat = new Flat()
            {
                Name = "TestFlat2WithoutSessionUser",
                Address = "Address2",
                Creator = user,
            };
            context.Flats.Add(flat);

            Housework housework = new Housework()
            {
                Name = "HouseworkWithousSessionUser",
                FlatId = 2,
                Description = "Desc",
                HouseworkSettings = new HouseworkSettings()
                {
                    Days = "1",
                    FrequencyId = (int)Consts.HouseworkFrequencies.Once,
                    HouseworkId = 1
                }
            };
            context.Houseworks.Add(housework);

            context.SaveChanges();
        }
    }
}