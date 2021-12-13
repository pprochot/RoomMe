using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class FKforproducts : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "AuthorId",
                table: "products");

            migrationBuilder.AddColumn<int>(
                name: "AuthorIdId",
                table: "products",
                type: "int",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_products_AuthorIdId",
                table: "products",
                column: "AuthorIdId");

            migrationBuilder.AddForeignKey(
                name: "FK_products_users_AuthorIdId",
                table: "products",
                column: "AuthorIdId",
                principalTable: "users",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_products_users_AuthorIdId",
                table: "products");

            migrationBuilder.DropIndex(
                name: "IX_products_AuthorIdId",
                table: "products");

            migrationBuilder.DropColumn(
                name: "AuthorIdId",
                table: "products");

            migrationBuilder.AddColumn<int>(
                name: "AuthorId",
                table: "products",
                type: "int",
                nullable: false,
                defaultValue: 0);
        }
    }
}
