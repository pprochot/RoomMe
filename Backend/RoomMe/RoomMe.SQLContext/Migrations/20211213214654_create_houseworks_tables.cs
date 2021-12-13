using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace RoomMe.SQLContext.Migrations
{
    public partial class create_houseworks_tables : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "HouseworkFrequencies",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    Value = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_HouseworkFrequencies", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Houseworks",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    FlatId = table.Column<int>(type: "int", nullable: false),
                    AuthorId = table.Column<int>(type: "int", nullable: false),
                    Name = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    Description = table.Column<string>(type: "nvarchar(max)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Houseworks", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Houseworks_Flats_FlatId",
                        column: x => x.FlatId,
                        principalTable: "Flats",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Houseworks_Users_AuthorId",
                        column: x => x.AuthorId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "HouseworkStatuses",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(type: "nvarchar(max)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_HouseworkStatuses", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "HouseworkSettings",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    HouseworkId = table.Column<int>(type: "int", nullable: false),
                    FrequencyId = table.Column<int>(type: "int", nullable: false),
                    Day = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_HouseworkSettings", x => x.Id);
                    table.ForeignKey(
                        name: "FK_HouseworkSettings_HouseworkFrequencies_FrequencyId",
                        column: x => x.FrequencyId,
                        principalTable: "HouseworkFrequencies",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_HouseworkSettings_Houseworks_HouseworkId",
                        column: x => x.HouseworkId,
                        principalTable: "Houseworks",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "HouseworkUser",
                columns: table => new
                {
                    HouseworksId = table.Column<int>(type: "int", nullable: false),
                    UsersId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_HouseworkUser", x => new { x.HouseworksId, x.UsersId });
                    table.ForeignKey(
                        name: "FK_HouseworkUser_Houseworks_HouseworksId",
                        column: x => x.HouseworksId,
                        principalTable: "Houseworks",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_HouseworkUser_Users_UsersId",
                        column: x => x.UsersId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "HouseworkSchedules",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    HouseworkId = table.Column<int>(type: "int", nullable: false),
                    UserId = table.Column<int>(type: "int", nullable: false),
                    StatusId = table.Column<int>(type: "int", nullable: false),
                    HouseworkStatusId = table.Column<int>(type: "int", nullable: true),
                    Date = table.Column<DateTime>(type: "datetime2", nullable: false),
                    HouseworkSettingsId = table.Column<int>(type: "int", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_HouseworkSchedules", x => x.Id);
                    table.ForeignKey(
                        name: "FK_HouseworkSchedules_Houseworks_HouseworkId",
                        column: x => x.HouseworkId,
                        principalTable: "Houseworks",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_HouseworkSchedules_HouseworkSettings_HouseworkSettingsId",
                        column: x => x.HouseworkSettingsId,
                        principalTable: "HouseworkSettings",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_HouseworkSchedules_HouseworkStatuses_HouseworkStatusId",
                        column: x => x.HouseworkStatusId,
                        principalTable: "HouseworkStatuses",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_HouseworkSchedules_Users_UserId",
                        column: x => x.UserId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Houseworks_AuthorId",
                table: "Houseworks",
                column: "AuthorId",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Houseworks_FlatId",
                table: "Houseworks",
                column: "FlatId");

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkSchedules_HouseworkId",
                table: "HouseworkSchedules",
                column: "HouseworkId");

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkSchedules_HouseworkSettingsId",
                table: "HouseworkSchedules",
                column: "HouseworkSettingsId");

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkSchedules_HouseworkStatusId",
                table: "HouseworkSchedules",
                column: "HouseworkStatusId");

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkSchedules_UserId",
                table: "HouseworkSchedules",
                column: "UserId");

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkSettings_FrequencyId",
                table: "HouseworkSettings",
                column: "FrequencyId");

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkSettings_HouseworkId",
                table: "HouseworkSettings",
                column: "HouseworkId");

            migrationBuilder.CreateIndex(
                name: "IX_HouseworkUser_UsersId",
                table: "HouseworkUser",
                column: "UsersId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "HouseworkSchedules");

            migrationBuilder.DropTable(
                name: "HouseworkUser");

            migrationBuilder.DropTable(
                name: "HouseworkSettings");

            migrationBuilder.DropTable(
                name: "HouseworkStatuses");

            migrationBuilder.DropTable(
                name: "HouseworkFrequencies");

            migrationBuilder.DropTable(
                name: "Houseworks");
        }
    }
}
