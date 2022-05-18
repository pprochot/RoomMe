using System;
using Microsoft.EntityFrameworkCore;
using RoomMe.SQLContext.Dictionaries;
using RoomMe.SQLContext.Models;

namespace RoomMe.SQLContext
{
    public class SqlContext: DbContext
    {
        public SqlContext(DbContextOptions<SqlContext> options) : base(options) { }

        public DbSet<User> Users { get; set; }
        public DbSet<Product> Products { get; set; }
        public DbSet<Flat> Flats { get; set; }
        public DbSet<Housework> Houseworks { get; set; }
        public DbSet<HouseworkSchedule> HouseworkSchedules { get; set; }
        public DbSet<HouseworkFrequency> HouseworkFrequencies { get; set; }
        public DbSet<HouseworkSettings> HouseworkSettings { get; set; }
        public DbSet<HouseworkStatus> HouseworkStatuses { get; set; }
        public DbSet<PrivateNotification> PrivateNotifications { get; set; }
        public DbSet<FlatNotification> FlatNotifications { get; set; }
        public DbSet<NotificationSettings> NotificationSettings { get; set; }
        public DbSet<NotificationFrequency> NotificationFrequencies { get; set; }
        public DbSet<PrivateCost> PrivateCosts { get; set; }
        public DbSet<CommonCost> CommonCosts { get; set; }
        public DbSet<ShoppingList> ShoppingLists { get; set; }
        public DbSet<Receipt> Receipts { get; set; }
        public DbSet<RentCost> RentCosts { get; set; }
        public DbSet<UserFriend> UserFriends { get; set; }
        public DbSet<RefreshToken> RefreshTokens { get; set; }
        public DbSet<StatisticsFrequency> StatisticsFrequencies { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            DictionaryBuilder.AddDictionaries(modelBuilder);

            modelBuilder.Entity<Housework>().HasOne(x => x.Author).WithMany().OnDelete(DeleteBehavior.Restrict);
            modelBuilder.Entity<Housework>().HasMany(x => x.Users).WithMany(x => x.Houseworks);

            modelBuilder.Entity<Product>().HasOne(x => x.CommonCost).WithOne().OnDelete(DeleteBehavior.Restrict);
            modelBuilder.Entity<Product>().HasOne(x => x.ShoppingList).WithMany(y => y.Products).OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<RentCost>().HasKey(x => new { x.UserId, x.FlatId });

            modelBuilder.Entity<UserFriend>().HasKey(x => new { x.UserId, x.FriendId });
            modelBuilder.Entity<UserFriend>().HasOne(x => x.User).WithMany(y => y.Friends).OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<Flat>().HasOne(x => x.Creator).WithMany(y => y.OwnedFlats).OnDelete(DeleteBehavior.Restrict);
        }
    }
}
