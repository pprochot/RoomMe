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
docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=RoomMe22" ^
   -p 1433:1433 --name RoomMeDb -h RoomMeDb ^
   -d mcr.microsoft.com/mssql/server:2019-latest
   ```
2. Go to sqlcmd in Docker container

   First, run interactive bash of the container.
- Linux:
```
sudo docker exec -it sql1 "bash"
```
- Windows:
```
docker exec -it sql1 "bash"
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
