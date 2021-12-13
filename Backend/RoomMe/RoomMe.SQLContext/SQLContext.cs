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

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
        }
    }
}
