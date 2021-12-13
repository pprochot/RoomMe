
# RoomMe
## Development database setup on Docker
### This instructions show how to set up MSSQL database on all the system using Docker command line.

1. Run Docker container

   The following command will pull MSSQL image (if not present) and run container based on it:
- Linux:
```
sudo docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=RoomMe22" \
   -p 1433:1433 --name RoomMeDb -h RoomMeDb \
   -d mcr.microsoft.com/mssql/server:2019-latest
```
- Windows:
```
docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=RoomMe22" -p 1433:1433 --name RoomMeDb -h RoomMeDb -d mcr.microsoft.com/mssql/server:2019-latest
```
2. Go to sqlcmd in Docker container

   First, run interactive bash of the container.
- Linux:
```
sudo docker exec -it RoomMeDb "bash"
```
- Windows:
```
docker exec -it RoomMeDb "bash"
```

- Then run this:
```
/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P "RoomMe22"
```
   You should see in your terminal new line started with `1>`

3. Create database
```
CREATE DATABASE RoomMeDb
GO
```
- That's all, now you can connect to your database.

4. When you shut down created container, you can run it through Docker Desktop or using following command:
```
docker start RoomMeDb
```
## Backend - migrations
### Installing EF Core
1. Type in your shell
```
dotnet  tool install --global dotnet-ef
```
### Working with current state of DB
We want to work always with as fresh DB changes as possible:
1. After you prepared your database open Backend solution in Visual Studio 2019
2. Open Package Manager Console and type
```
update-database
```
to update your database to current iteration
### Creating new migration
1. After you made some changes in DB (e.g created new table) you should create new migration
2.  Open Package Manager Console and type
```
add-migration <<name>>
```
where `<<name>>` should be replaced with the name of migration (e.g `add-migration create_users_table`)

3. Verify whether the migration contains only changes that it should have (check new created file in `migrations` folder)
4. If step above is positive, update your database using `update-database` then commit your changes
5. If step 3. was negative, then revert your changes using
```
remove-migration
```
and try to fix changes, then repeat whole proccess
