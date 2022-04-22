using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class add_housework_dictionaries : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.InsertData(
                table: "HouseworkFrequencies",
                columns: new[] { "Id", "Name", "Value" },
                values: new object[,]
                {
                    { 1, "Once", 0 },
                    { 2, "Daily", 1 },
                    { 3, "Weekly", 7 },
                    { 4, "Twice a week", 3 },
                    { 5, "Monthly", 30 }
                });

            migrationBuilder.InsertData(
                table: "HouseworkStatuses",
                columns: new[] { "Id", "Name" },
                values: new object[,]
                {
                    { 1, "In Progress" },
                    { 2, "Done" },
                    { 3, "Expired" },
                    { 4, "Delayed" }
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DeleteData(
                table: "HouseworkFrequencies",
                keyColumn: "Id",
                keyValue: 1);

            migrationBuilder.DeleteData(
                table: "HouseworkFrequencies",
                keyColumn: "Id",
                keyValue: 2);

            migrationBuilder.DeleteData(
                table: "HouseworkFrequencies",
                keyColumn: "Id",
                keyValue: 3);

            migrationBuilder.DeleteData(
                table: "HouseworkFrequencies",
                keyColumn: "Id",
                keyValue: 4);

            migrationBuilder.DeleteData(
                table: "HouseworkFrequencies",
                keyColumn: "Id",
                keyValue: 5);

            migrationBuilder.DeleteData(
                table: "HouseworkStatuses",
                keyColumn: "Id",
                keyValue: 1);

            migrationBuilder.DeleteData(
                table: "HouseworkStatuses",
                keyColumn: "Id",
                keyValue: 2);

            migrationBuilder.DeleteData(
                table: "HouseworkStatuses",
                keyColumn: "Id",
                keyValue: 3);

            migrationBuilder.DeleteData(
                table: "HouseworkStatuses",
                keyColumn: "Id",
                keyValue: 4);
        }
    }
}
