using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class add_creatorId_to_users : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<int>(
                name: "CreatorId",
                table: "Flats",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.CreateIndex(
                name: "IX_Flats_CreatorId",
                table: "Flats",
                column: "CreatorId");

            migrationBuilder.AddForeignKey(
                name: "FK_Flats_Users_CreatorId",
                table: "Flats",
                column: "CreatorId",
                principalTable: "Users",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Flats_Users_CreatorId",
                table: "Flats");

            migrationBuilder.DropIndex(
                name: "IX_Flats_CreatorId",
                table: "Flats");

            migrationBuilder.DropColumn(
                name: "CreatorId",
                table: "Flats");
        }
    }
}
