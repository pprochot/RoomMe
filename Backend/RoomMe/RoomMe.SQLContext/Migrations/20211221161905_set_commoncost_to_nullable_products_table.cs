using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class set_commoncost_to_nullable_products_table : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_Products_CommonCostId",
                table: "Products");

            migrationBuilder.AlterColumn<int>(
                name: "CommonCostId",
                table: "Products",
                type: "int",
                nullable: true,
                oldClrType: typeof(int),
                oldType: "int");

            migrationBuilder.CreateIndex(
                name: "IX_Products_CommonCostId",
                table: "Products",
                column: "CommonCostId",
                unique: true,
                filter: "[CommonCostId] IS NOT NULL");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_Products_CommonCostId",
                table: "Products");

            migrationBuilder.AlterColumn<int>(
                name: "CommonCostId",
                table: "Products",
                type: "int",
                nullable: false,
                defaultValue: 0,
                oldClrType: typeof(int),
                oldType: "int",
                oldNullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_Products_CommonCostId",
                table: "Products",
                column: "CommonCostId",
                unique: true);
        }
    }
}
