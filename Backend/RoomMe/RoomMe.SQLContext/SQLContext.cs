﻿using System;
using Microsoft.EntityFrameworkCore;
using RoomMe.SQLContext.Models;

namespace RoomMe.SQLContext
{
    public class SQLContext: DbContext
    {
        public SQLContext(DbContextOptions<SQLContext> options) : base(options) { }

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

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Housework>().HasOne(x => x.Author).WithOne().OnDelete(DeleteBehavior.Restrict);
            modelBuilder.Entity<Housework>().HasMany(x => x.Users).WithMany(x => x.Houseworks);
        }
    }
}
