using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class set_completor_to_nullable_shopping_list_table : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_ShoppingLists_Users_CompletorId",
                table: "ShoppingLists");

            migrationBuilder.AlterColumn<int>(
                name: "CompletorId",
                table: "ShoppingLists",
                type: "int",
                nullable: true,
                oldClrType: typeof(int),
                oldType: "int");

            migrationBuilder.AddForeignKey(
                name: "FK_ShoppingLists_Users_CompletorId",
                table: "ShoppingLists",
                column: "CompletorId",
                principalTable: "Users",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_ShoppingLists_Users_CompletorId",
                table: "ShoppingLists");

            migrationBuilder.AlterColumn<int>(
                name: "CompletorId",
                table: "ShoppingLists",
                type: "int",
                nullable: false,
                defaultValue: 0,
                oldClrType: typeof(int),
                oldType: "int",
                oldNullable: true);

            migrationBuilder.AddForeignKey(
                name: "FK_ShoppingLists_Users_CompletorId",
                table: "ShoppingLists",
                column: "CompletorId",
                principalTable: "Users",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
