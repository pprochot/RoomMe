using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class add_columns_to_receipts_table : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "Extension",
                table: "Receipts",
                type: "nvarchar(max)",
                nullable: true);

            migrationBuilder.AddColumn<Guid>(
                name: "Guid",
                table: "Receipts",
                type: "uniqueidentifier",
                nullable: false,
                defaultValue: new Guid("00000000-0000-0000-0000-000000000000"));

            migrationBuilder.AddColumn<string>(
                name: "Name",
                table: "Receipts",
                type: "nvarchar(max)",
                nullable: true);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Extension",
                table: "Receipts");

            migrationBuilder.DropColumn(
                name: "Guid",
                table: "Receipts");

            migrationBuilder.DropColumn(
                name: "Name",
                table: "Receipts");
        }
    }
}
