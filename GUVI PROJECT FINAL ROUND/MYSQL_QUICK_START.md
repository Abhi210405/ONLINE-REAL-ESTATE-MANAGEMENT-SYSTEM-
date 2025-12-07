# Quick MySQL Setup (5 Minutes)

**Goal:** Connect your MSP Real Estate System to MySQL database

---

## ⚡ Fastest Method: Docker (Recommended)

### Windows (PowerShell)

```powershell
# 1. Start MySQL Docker container
docker run -d --name mysql-server `
  -p 3306:3306 `
  -e MYSQL_ROOT_PASSWORD=mysql123 `
  -e MYSQL_DATABASE=msp_realestate `
  mysql:8.0

# 2. Wait 5 seconds for MySQL to start
Start-Sleep -Seconds 5

# 3. Verify it's running
docker logs mysql-server
```

### macOS / Linux

```bash
# 1. Start MySQL Docker container
docker run -d --name mysql-server \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=mysql123 \
  -e MYSQL_DATABASE=msp_realestate \
  mysql:8.0

# 2. Wait for it to start
sleep 5

# 3. Verify it's running
docker logs mysql-server
```

---

## 📝 Configure Application

### Step 1: Edit `database.properties`

Open the file in your editor and change:

```properties
# FROM:
DB_TYPE=sqlite

# TO:
DB_TYPE=mysql

# Keep the rest as default (or adjust if needed):
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_USER=root
MYSQL_PASSWORD=mysql123
```

### Step 2: Rebuild Project

```bash
cd "C:\Users\Abhi Nandan Pandey\OneDrive\Desktop\GUVI PROJECT"
mvn clean package
```

### Step 3: Run Application

```bash
java -jar target/msp-realestate-1.0-SNAPSHOT.jar
```

✅ **Done!** Your app now uses MySQL instead of SQLite.

---

## ✅ Verify Connection

When you start the app, you should see:

```
✓ Database configuration loaded: DB_TYPE=mysql
✓ Database schema initialized successfully
  Using: MySQL: root@localhost:3306/msp_realestate
```

---

## 🔄 Switch Back to SQLite

Just change `database.properties` back:

```properties
DB_TYPE=sqlite
```

Then rebuild and run. Your SQLite data is still there!

---

## 🛑 Stop MySQL (Docker)

```bash
docker stop mysql-server
docker rm mysql-server
```

---

## 🆘 Troubleshooting

**Q: "Connection refused"**  
A: Make sure Docker is running: `docker ps`

**Q: "Unknown database"**  
A: MySQL container needs more time to start. Wait 10 seconds and try again.

**Q: "Access denied"**  
A: Check password in `database.properties` matches Docker command.

---

## That's It! 🎉

Your MSP Real Estate System is now connected to MySQL!

For more details, see: [MYSQL_INTEGRATION_GUIDE.md](MYSQL_INTEGRATION_GUIDE.md)
