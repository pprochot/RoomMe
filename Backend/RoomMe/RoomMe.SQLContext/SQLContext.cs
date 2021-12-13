using System;
using Microsoft.EntityFrameworkCore;
using RoomMe.SQLContext.Models;

namespace RoomMe.SQLContext
{
    public class SQLContext: DbContext
    {
        public SQLContext(DbContextOptions<SQLContext> options) : base(options) { }

        public DbSet<User> users { get; set; }
        public DbSet<Product> products { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Product>()
                .HasOne(p => p.Author);
        }
    }
}
