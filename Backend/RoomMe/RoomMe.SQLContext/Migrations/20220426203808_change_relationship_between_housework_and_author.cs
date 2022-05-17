using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class change_relationship_between_housework_and_author : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_Houseworks_AuthorId",
                table: "Houseworks");

            migrationBuilder.CreateIndex(
                name: "IX_Houseworks_AuthorId",
                table: "Houseworks",
                column: "AuthorId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_Houseworks_AuthorId",
                table: "Houseworks");

            migrationBuilder.CreateIndex(
                name: "IX_Houseworks_AuthorId",
                table: "Houseworks",
                column: "AuthorId",
                unique: true);
        }
    }
}
