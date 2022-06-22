using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class remove_monthly_frequency : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DeleteData(
                table: "HouseworkFrequencies",
                keyColumn: "Id",
                keyValue: 5);

            migrationBuilder.DropColumn(
                name: "Day",
                table: "HouseworkSettings");

            migrationBuilder.AddColumn<string>(
                name: "Days",
                table: "HouseworkSettings",
                type: "nvarchar(max)",
                nullable: true);

            migrationBuilder.UpdateData(
                table: "HouseworkStatuses",
                keyColumn: "Id",
                keyValue: 1,
                column: "Name",
                value: "Todo");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Days",
                table: "HouseworkSettings");

            migrationBuilder.AddColumn<int>(
                name: "Day",
                table: "HouseworkSettings",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.InsertData(
                table: "HouseworkFrequencies",
                columns: new[] { "Id", "Name", "Value" },
                values: new object[] { 5, "Monthly", 30 });

            migrationBuilder.UpdateData(
                table: "HouseworkStatuses",
                keyColumn: "Id",
                keyValue: 1,
                column: "Name",
                value: "In Progress");
        }
    }
}
