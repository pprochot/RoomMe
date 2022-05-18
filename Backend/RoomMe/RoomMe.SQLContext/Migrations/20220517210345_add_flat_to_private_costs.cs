using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class add_flat_to_private_costs : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<int>(
                name: "FlatId",
                table: "PrivateCosts",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.CreateIndex(
                name: "IX_PrivateCosts_FlatId",
                table: "PrivateCosts",
                column: "FlatId");

            migrationBuilder.AddForeignKey(
                name: "FK_PrivateCosts_Flats_FlatId",
                table: "PrivateCosts",
                column: "FlatId",
                principalTable: "Flats",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_PrivateCosts_Flats_FlatId",
                table: "PrivateCosts");

            migrationBuilder.DropIndex(
                name: "IX_PrivateCosts_FlatId",
                table: "PrivateCosts");

            migrationBuilder.DropColumn(
                name: "FlatId",
                table: "PrivateCosts");
        }
    }
}
