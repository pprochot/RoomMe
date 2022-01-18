﻿// <auto-generated />
using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using RoomMe.SQLContext;

namespace RoomMe.SQLContext.Migrations
{
    [DbContext(typeof(SqlContext))]
    partial class SQLContextModelSnapshot : ModelSnapshot
    {
        protected override void BuildModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("ProductVersion", "5.0.12")
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("FlatUser", b =>
                {
                    b.Property<int>("FlatsId")
                        .HasColumnType("int");

                    b.Property<int>("UsersId")
                        .HasColumnType("int");

                    b.HasKey("FlatsId", "UsersId");

                    b.HasIndex("UsersId");

                    b.ToTable("FlatUser");
                });

            modelBuilder.Entity("HouseworkUser", b =>
                {
                    b.Property<int>("HouseworksId")
                        .HasColumnType("int");

                    b.Property<int>("UsersId")
                        .HasColumnType("int");

                    b.HasKey("HouseworksId", "UsersId");

                    b.HasIndex("UsersId");

                    b.ToTable("HouseworkUser");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.CommonCost", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<DateTime>("Date")
                        .HasColumnType("datetime2");

                    b.Property<string>("Description")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("FlatId")
                        .HasColumnType("int");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.Property<double>("Value")
                        .HasColumnType("float");

                    b.HasKey("Id");

                    b.HasIndex("FlatId");

                    b.HasIndex("UserId");

                    b.ToTable("CommonCosts");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.Flat", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Address")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.ToTable("Flats");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.FlatNotification", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Description")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("FlatId")
                        .HasColumnType("int");

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("SettingsId")
                        .HasColumnType("int");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("FlatId");

                    b.HasIndex("SettingsId");

                    b.HasIndex("UserId");

                    b.ToTable("FlatNotifications");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.Housework", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("AuthorId")
                        .HasColumnType("int");

                    b.Property<string>("Description")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("FlatId")
                        .HasColumnType("int");

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.HasIndex("AuthorId")
                        .IsUnique();

                    b.HasIndex("FlatId");

                    b.ToTable("Houseworks");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.HouseworkFrequency", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("Value")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.ToTable("HouseworkFrequencies");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.HouseworkSchedule", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<DateTime>("Date")
                        .HasColumnType("datetime2");

                    b.Property<int>("HouseworkId")
                        .HasColumnType("int");

                    b.Property<int?>("HouseworkStatusId")
                        .HasColumnType("int");

                    b.Property<int>("StatusId")
                        .HasColumnType("int");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("HouseworkId");

                    b.HasIndex("HouseworkStatusId");

                    b.HasIndex("UserId");

                    b.ToTable("HouseworkSchedules");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.HouseworkSettings", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("Day")
                        .HasColumnType("int");

                    b.Property<int>("FrequencyId")
                        .HasColumnType("int");

                    b.Property<int>("HouseworkId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("FrequencyId");

                    b.HasIndex("HouseworkId")
                        .IsUnique();

                    b.ToTable("HouseworkSettings");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.HouseworkStatus", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.ToTable("HouseworkStatuses");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.NotificationFrequency", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("Value")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.ToTable("NotificationFrequencies");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.NotificationSettings", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<DateTime>("BegginingDate")
                        .HasColumnType("datetime2");

                    b.Property<int>("FrequencyId")
                        .HasColumnType("int");

                    b.Property<int>("Repetitions")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("FrequencyId");

                    b.ToTable("NotificationSettings");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.PrivateCost", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<DateTime>("Date")
                        .HasColumnType("datetime2");

                    b.Property<string>("Description")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.Property<double>("Value")
                        .HasColumnType("float");

                    b.HasKey("Id");

                    b.HasIndex("UserId");

                    b.ToTable("PrivateCosts");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.PrivateNotification", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Description")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("SettingsId")
                        .HasColumnType("int");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("SettingsId");

                    b.HasIndex("UserId");

                    b.ToTable("PrivateNotifications");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.Product", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("AuthorId")
                        .HasColumnType("int");

                    b.Property<bool>("Bought")
                        .HasColumnType("bit");

                    b.Property<int?>("CommonCostId")
                        .HasColumnType("int");

                    b.Property<string>("Description")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("Quantity")
                        .HasColumnType("int");

                    b.Property<string>("Reason")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("ShoppingListId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("AuthorId");

                    b.HasIndex("CommonCostId")
                        .IsUnique()
                        .HasFilter("[CommonCostId] IS NOT NULL");

                    b.HasIndex("ShoppingListId");

                    b.ToTable("Products");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.Receipt", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Path")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("ShoppingListId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("ShoppingListId");

                    b.ToTable("Receipts");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.RentCost", b =>
                {
                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.Property<int>("FlatId")
                        .HasColumnType("int");

                    b.Property<DateTime>("CreationDate")
                        .HasColumnType("datetime2");

                    b.Property<double>("Value")
                        .HasColumnType("float");

                    b.HasKey("UserId", "FlatId");

                    b.HasIndex("FlatId");

                    b.ToTable("RentCosts");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.ShoppingList", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<DateTime>("CompletionDate")
                        .HasColumnType("datetime2");

                    b.Property<int?>("CompletorId")
                        .HasColumnType("int");

                    b.Property<DateTime>("CreationDate")
                        .HasColumnType("datetime2");

                    b.Property<string>("Description")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("FlatId")
                        .HasColumnType("int");

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.HasIndex("CompletorId");

                    b.HasIndex("FlatId");

                    b.ToTable("ShoppingLists");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.User", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Email")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Firstname")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Lastname")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Nickname")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Password")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("PhoneNumber")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id");

                    b.ToTable("Users");
                });

            modelBuilder.Entity("FlatUser", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.Flat", null)
                        .WithMany()
                        .HasForeignKey("FlatsId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("RoomMe.SQLContext.Models.User", null)
                        .WithMany()
                        .HasForeignKey("UsersId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("HouseworkUser", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.Housework", null)
                        .WithMany()
                        .HasForeignKey("HouseworksId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("RoomMe.SQLContext.Models.User", null)
                        .WithMany()
                        .HasForeignKey("UsersId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.CommonCost", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.Flat", "Flat")
                        .WithMany("Costs")
                        .HasForeignKey("FlatId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("RoomMe.SQLContext.Models.User", "User")
                        .WithMany("CommonCosts")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Flat");

                    b.Navigation("User");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.FlatNotification", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.Flat", "Flat")
                        .WithMany("Notifications")
                        .HasForeignKey("FlatId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("RoomMe.SQLContext.Models.NotificationSettings", "Settings")
                        .WithMany()
                        .HasForeignKey("SettingsId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("RoomMe.SQLContext.Models.User", "User")
                        .WithMany("FlatNotifications")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Flat");

                    b.Navigation("Settings");

                    b.Navigation("User");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.Housework", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.User", "Author")
                        .WithOne()
                        .HasForeignKey("RoomMe.SQLContext.Models.Housework", "AuthorId")
                        .OnDelete(DeleteBehavior.Restrict)
                        .IsRequired();

                    b.HasOne("RoomMe.SQLContext.Models.Flat", "Flat")
                        .WithMany("Houseworks")
                        .HasForeignKey("FlatId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Author");

                    b.Navigation("Flat");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.HouseworkSchedule", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.Housework", "Housework")
                        .WithMany("HouseworkSchedules")
                        .HasForeignKey("HouseworkId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("RoomMe.SQLContext.Models.HouseworkStatus", "HouseworkStatus")
                        .WithMany()
                        .HasForeignKey("HouseworkStatusId");

                    b.HasOne("RoomMe.SQLContext.Models.User", "User")
                        .WithMany()
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Housework");

                    b.Navigation("HouseworkStatus");

                    b.Navigation("User");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.HouseworkSettings", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.HouseworkFrequency", "Frequency")
                        .WithMany()
                        .HasForeignKey("FrequencyId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("RoomMe.SQLContext.Models.Housework", "Housework")
                        .WithOne("HouseworkSettings")
                        .HasForeignKey("RoomMe.SQLContext.Models.HouseworkSettings", "HouseworkId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Frequency");

                    b.Navigation("Housework");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.NotificationSettings", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.NotificationFrequency", "Frequency")
                        .WithMany()
                        .HasForeignKey("FrequencyId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Frequency");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.PrivateCost", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.User", "User")
                        .WithMany("PrivateCosts")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("User");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.PrivateNotification", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.NotificationSettings", "Settings")
                        .WithMany()
                        .HasForeignKey("SettingsId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("RoomMe.SQLContext.Models.User", "User")
                        .WithMany("PrivateNotifications")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Settings");

                    b.Navigation("User");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.Product", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.User", "Author")
                        .WithMany("Products")
                        .HasForeignKey("AuthorId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("RoomMe.SQLContext.Models.CommonCost", "CommonCost")
                        .WithOne()
                        .HasForeignKey("RoomMe.SQLContext.Models.Product", "CommonCostId")
                        .OnDelete(DeleteBehavior.Restrict);

                    b.HasOne("RoomMe.SQLContext.Models.ShoppingList", "ShoppingList")
                        .WithMany("Products")
                        .HasForeignKey("ShoppingListId")
                        .OnDelete(DeleteBehavior.Restrict)
                        .IsRequired();

                    b.Navigation("Author");

                    b.Navigation("CommonCost");

                    b.Navigation("ShoppingList");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.Receipt", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.ShoppingList", null)
                        .WithMany("Receipts")
                        .HasForeignKey("ShoppingListId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.RentCost", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.Flat", "Flat")
                        .WithMany("RentCosts")
                        .HasForeignKey("FlatId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("RoomMe.SQLContext.Models.User", "User")
                        .WithMany()
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Flat");

                    b.Navigation("User");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.ShoppingList", b =>
                {
                    b.HasOne("RoomMe.SQLContext.Models.User", "Completor")
                        .WithMany("ShoppingLists")
                        .HasForeignKey("CompletorId");

                    b.HasOne("RoomMe.SQLContext.Models.Flat", "Flat")
                        .WithMany()
                        .HasForeignKey("FlatId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Completor");

                    b.Navigation("Flat");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.Flat", b =>
                {
                    b.Navigation("Costs");

                    b.Navigation("Houseworks");

                    b.Navigation("Notifications");

                    b.Navigation("RentCosts");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.Housework", b =>
                {
                    b.Navigation("HouseworkSchedules");

                    b.Navigation("HouseworkSettings");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.ShoppingList", b =>
                {
                    b.Navigation("Products");

                    b.Navigation("Receipts");
                });

            modelBuilder.Entity("RoomMe.SQLContext.Models.User", b =>
                {
                    b.Navigation("CommonCosts");

                    b.Navigation("FlatNotifications");

                    b.Navigation("PrivateCosts");

                    b.Navigation("PrivateNotifications");

                    b.Navigation("Products");

                    b.Navigation("ShoppingLists");
                });
#pragma warning restore 612, 618
        }
    }
}
