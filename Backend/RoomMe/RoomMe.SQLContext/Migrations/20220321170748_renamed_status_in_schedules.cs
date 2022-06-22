using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class renamed_status_in_schedules : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_HouseworkSchedules_HouseworkStatuses_HouseworkStatusId",
                table: "HouseworkSchedules");

            migrationBuilder.DropIndex(
                name: "IX_HouseworkSchedules_HouseworkStatusId",
                table: "HouseworkSchedules");

            migrationBuilder.DropColumn(
                name: "HouseworkStatusId",
                table: "HouseworkSchedules");

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkSchedules_StatusId",
                table: "HouseworkSchedules",
                column: "StatusId");

            migrationBuilder.AddForeignKey(
                name: "FK_HouseworkSchedules_HouseworkStatuses_StatusId",
                table: "HouseworkSchedules",
                column: "StatusId",
                principalTable: "HouseworkStatuses",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_HouseworkSchedules_HouseworkStatuses_StatusId",
                table: "HouseworkSchedules");

            migrationBuilder.DropIndex(
                name: "IX_HouseworkSchedules_StatusId",
                table: "HouseworkSchedules");

            migrationBuilder.AddColumn<int>(
                name: "HouseworkStatusId",
                table: "HouseworkSchedules",
                type: "int",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkSchedules_HouseworkStatusId",
                table: "HouseworkSchedules",
                column: "HouseworkStatusId");

            migrationBuilder.AddForeignKey(
                name: "FK_HouseworkSchedules_HouseworkStatuses_HouseworkStatusId",
                table: "HouseworkSchedules",
                column: "HouseworkStatusId",
                principalTable: "HouseworkStatuses",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
