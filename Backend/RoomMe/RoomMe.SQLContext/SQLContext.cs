﻿using System;
using Microsoft.EntityFrameworkCore;
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

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Housework>().HasOne(x => x.Author).WithOne().OnDelete(DeleteBehavior.Restrict);
            modelBuilder.Entity<Housework>().HasMany(x => x.Users).WithMany(x => x.Houseworks);

            modelBuilder.Entity<Product>().HasOne(x => x.CommonCost).WithOne().OnDelete(DeleteBehavior.Restrict);
            modelBuilder.Entity<Product>().HasOne(x => x.ShoppingList).WithMany(y => y.Products).OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<RentCost>().HasKey(x => new { x.UserId, x.FlatId });
        }
    }
}
