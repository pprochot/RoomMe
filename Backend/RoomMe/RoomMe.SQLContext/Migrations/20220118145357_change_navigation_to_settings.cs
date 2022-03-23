using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class change_navigation_to_settings : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_HouseworkSchedules_HouseworkSettings_HouseworkSettingsId",
                table: "HouseworkSchedules");

            migrationBuilder.DropIndex(
                name: "IX_HouseworkSettings_HouseworkId",
                table: "HouseworkSettings");

            migrationBuilder.DropIndex(
                name: "IX_HouseworkSchedules_HouseworkSettingsId",
                table: "HouseworkSchedules");

            migrationBuilder.DropColumn(
                name: "HouseworkSettingsId",
                table: "HouseworkSchedules");

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkSettings_HouseworkId",
                table: "HouseworkSettings",
                column: "HouseworkId",
                unique: true);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_HouseworkSettings_HouseworkId",
                table: "HouseworkSettings");

            migrationBuilder.AddColumn<int>(
                name: "HouseworkSettingsId",
                table: "HouseworkSchedules",
                type: "int",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkSettings_HouseworkId",
                table: "HouseworkSettings",
                column: "HouseworkId");

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkSchedules_HouseworkSettingsId",
                table: "HouseworkSchedules",
                column: "HouseworkSettingsId");

            migrationBuilder.AddForeignKey(
                name: "FK_HouseworkSchedules_HouseworkSettings_HouseworkSettingsId",
                table: "HouseworkSchedules",
                column: "HouseworkSettingsId",
                principalTable: "HouseworkSettings",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
