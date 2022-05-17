using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class create_statistics_frequencies_table : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "StatisticsFrequencies",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(type: "nvarchar(max)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_StatisticsFrequencies", x => x.Id);
                });

            migrationBuilder.InsertData(
                table: "StatisticsFrequencies",
                columns: new[] { "Id", "Name" },
                values: new object[,]
                {
                    { 1, "All costs" },
                    { 2, "Daily" },
                    { 3, "Weekly" },
                    { 4, "Monthly" }
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "StatisticsFrequencies");
        }
    }
}
