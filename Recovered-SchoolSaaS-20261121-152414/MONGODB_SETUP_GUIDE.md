# SchoolSaaS Local MongoDB Setup & Usage Guide

This guide explains how to start, verify, manage, and troubleshoot the local MongoDB instance used by the SchoolSaaS Spring Boot microservices.

---

## 1. Prerequisites

- **Docker Desktop** installed on Windows.
- The `docker-compose.yml` file located in:
  ```text
  C:\Users\VAIS-DT4\Desktop\DonotDeleteIt\Recovered-SchoolSaaS-20261121-152414\docker-compose.yml
  ```

---

## 2. Daily Startup Workflow (Before Running Microservices)

### Step 1: Start Docker Desktop
1. Open **Docker Desktop** from the Windows Start menu (wait until the bottom-left status shows **Engine running**).

### Step 2: Start MongoDB
Open PowerShell or Command Prompt in `C:\Users\VAIS-DT4\Desktop\DonotDeleteIt\Recovered-SchoolSaaS-20261121-152414` and run:

```powershell
docker compose up -d
```

> **Note:** Because `restart: unless-stopped` is configured, MongoDB will automatically restart whenever Docker Desktop starts unless you explicitly stop it.

### Step 3: Verify MongoDB Is Running
```powershell
docker ps
```
You should see `school-saas-mongodb` with status `Up` and ports `0.0.0.0:27017->27017/tcp`.

You can also test a database ping:
```powershell
docker exec school-saas-mongodb mongosh --eval "db.adminCommand('ping')"
```
Expected output: `{ ok: 1 }`.

### Step 4: Start Your Microservices in IntelliJ IDEA
Start your Spring Boot applications in IntelliJ IDEA. Each service will automatically connect to `localhost:27017` and create its database on first write:

| Microservice | Port | Database Name (`localhost:27017`) |
| :--- | :--- | :--- |
| `AcademicMain` | `8081` | `sas_academicMain` |
| `NewLibrary` | `8082` | `sas_libraryDatabase` |
| `RoutineMain` | `8084` | `sas_routineMain` |
| `Admission` | `8085` | `sas_schoolAdmissionMain` |
| `Settings` | `8090` | `sas_setting` |
| `auth-service` | `8092` | `auth_sas` |
| `Result` | `8094` | `sas_result1` |
| `SchoolAccount` | `8099` | `sas_schoolAccount` |
| `teacher` | `8089` | `sas_teachersdb` |

---

## 2.5 Re-Syncing Latest Data from Shared LAN MongoDB (`192.168.11.157:27017`)

Whenever you want to refresh your local Docker MongoDB copy with the latest data from `192.168.11.157:27017` without touching the shared server, run this in PowerShell:

```powershell
$dbs = @(
  "auth_sas","sas_academicMain","sas_schoolAdmissionMain",
  "sas_libraryDatabase","sas_result1","sas_routineMain",
  "sas_schoolAccount","sas_setting","sas_teachersdb"
)
foreach ($db in $dbs) {
  docker exec school-saas-mongodb mongodump --uri="mongodb://192.168.11.157:27017/?directConnection=true" --db=$db --archive="/tmp/$db.archive"
  docker exec school-saas-mongodb mongorestore --uri="mongodb://localhost:27017" --drop --archive="/tmp/$db.archive"
  docker exec school-saas-mongodb rm -f "/tmp/$db.archive"
}
```

---

## 3. Common Commands

Run all commands from `C:\Users\VAIS-DT4\Desktop\DonotDeleteIt\Recovered-SchoolSaaS-20261121-152414`:

| Action | Command |
| :--- | :--- |
| **Start MongoDB in background** | `docker compose up -d` |
| **Stop MongoDB (keeps all data)** | `docker compose stop` |
| **Start stopped container again** | `docker compose start` |
| **Stop & remove container (keeps data volume)** | `docker compose down` |
| **View MongoDB logs** | `docker logs -f school-saas-mongodb` |
| **Open MongoDB Shell (`mongosh`)** | `docker exec -it school-saas-mongodb mongosh` |
| **List all databases** | `docker exec school-saas-mongodb mongosh --eval "show dbs"` |

> **Warning — Data Persistence:** Your database files are stored in the Docker volume `school_saas_mongo_data`. Running `docker compose down` keeps your data safe. **Do NOT run `docker compose down -v`** unless you intentionally want to wipe all local databases.

---

## 4. Connecting with MongoDB Compass (Optional GUI)

If you use **MongoDB Compass** to inspect collections and documents:
1. Click **New Connection**.
2. Enter the URI:
   ```text
   mongodb://localhost:27017
   ```
3. Click **Connect**.

---

## 5. Troubleshooting

### `Connection refused: getsockopt` on `localhost:27017`
1. Make sure **Docker Desktop** is running.
2. Check if the container is up:
   ```powershell
   docker ps -a --filter "name=school-saas-mongodb"
   ```
3. If the container is `Exited`, start it:
   ```powershell
   docker compose up -d
   ```

### `failed to connect to the docker API` / `dockerDesktopLinuxEngine` error
- Docker Desktop is not running yet. Launch **Docker Desktop** from the Start menu and wait ~15–30 seconds until it finishes initializing, then re-run `docker compose up -d`.

### Port `27017` already in use
- Check what process is using port `27017`:
  ```powershell
  netstat -ano | findstr :27017
  ```
- If another MongoDB service or container is already listening on `27017`, stop it before running `docker compose up -d`.
