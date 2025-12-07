# MySQL Integration Guide

**Project:** MSP Real Estate System  
**Status:** MySQL support added to existing SQLite application  
**Date:** November 24, 2025

---

## Overview

Your MSP Real Estate System now supports **two database backends**:
1. **SQLite** (Current default) — Embedded, no server required
2. **MySQL** (New) — Remote server, scalable enterprise database

**Switch between them by editing:** `database.properties`

---

## Quick Start: Switch to MySQL

### 1. Prerequisites

**Windows:**
```batch
# Option A: Install MySQL from: https://dev.mysql.com/downloads/mysql/

# Option B: Use Docker (recommended)
docker run -d --name mysql-server `
  -p 3306:3306 `
  -e MYSQL_ROOT_PASSWORD=yourpassword `
  -e MYSQL_DATABASE=msp_realestate `
  mysql:8.0
```

**Linux/macOS:**
```bash
# Option A: Install MySQL
sudo apt-get install mysql-server  # Ubuntu/Debian
brew install mysql                 # macOS

# Option B: Use Docker
docker run -d --name mysql-server \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=yourpassword \
  -e MYSQL_DATABASE=msp_realestate \
  mysql:8.0
```

### 2. Update Configuration

Edit `database.properties`:

```properties
# Change this line:
DB_TYPE=mysql

# Update these values (if using non-default MySQL):
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DATABASE=msp_realestate
MYSQL_USER=root
MYSQL_PASSWORD=yourpassword
```

### 3. Rebuild Project

```bash
mvn clean package
```

### 4. Run Application

```bash
java -jar target/msp-realestate-1.0-SNAPSHOT.jar
```

**That's it!** Your application now uses MySQL instead of SQLite.

---

## Detailed Setup Instructions

### MySQL Installation (All Platforms)

#### Windows

1. **Download:** https://dev.mysql.com/downloads/mysql/
2. **Install:** Run installer, choose default settings
3. **Verify:**
   ```batch
   mysql --version
   ```

#### macOS (Homebrew)

```bash
brew install mysql
brew services start mysql
mysql --version
```

#### Linux (Ubuntu/Debian)

```bash
sudo apt-get update
sudo apt-get install mysql-server
sudo mysql_secure_installation
mysql --version
```

#### Linux (Docker - Recommended)

```bash
docker run -d --name mysql-server \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=mypassword \
  -e MYSQL_DATABASE=msp_realestate \
  mysql:8.0
```

### Create Database Manually (Optional)

If not created automatically:

```bash
# Connect to MySQL
mysql -u root -p

# Inside MySQL CLI:
CREATE DATABASE msp_realestate;
SHOW DATABASES;
EXIT;
```

---

## Configuration Reference

### database.properties

```properties
# Database Type
DB_TYPE=mysql                    # Options: sqlite, mysql

# MySQL Connection Settings
MYSQL_HOST=localhost             # MySQL server hostname
MYSQL_PORT=3306                 # MySQL server port
MYSQL_DATABASE=msp_realestate   # Database name
MYSQL_USER=root                 # MySQL username
MYSQL_PASSWORD=mypassword       # MySQL password (blank if none)

# Connection Pool Settings
MAX_POOL_SIZE=10                # Maximum connections
MIN_POOL_SIZE=5                 # Minimum connections
CONNECTION_TIMEOUT=30000        # Connection timeout (ms)
IDLE_TIMEOUT=600000            # Idle timeout (ms)
```

### Example Configurations

**Local MySQL:**
```properties
DB_TYPE=mysql
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_USER=root
MYSQL_PASSWORD=root123
```

**Docker MySQL:**
```properties
DB_TYPE=mysql
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_USER=root
MYSQL_PASSWORD=dockerpassword
```

**Remote MySQL (AWS RDS, etc):**
```properties
DB_TYPE=mysql
MYSQL_HOST=myrds.amazonaws.com
MYSQL_PORT=3306
MYSQL_USER=admin
MYSQL_PASSWORD=rdspassword
```

---

## Migration from SQLite to MySQL

### Step 1: Backup SQLite Data

```bash
# Backup original database
cp msp_realestate.db msp_realestate.db.backup
```

### Step 2: Export SQLite Data

```bash
# Dump SQLite database to SQL file
sqlite3 msp_realestate.db .dump > sqlite_backup.sql
```

### Step 3: Import to MySQL

```bash
# Create MySQL database
mysql -u root -p -e "CREATE DATABASE msp_realestate;"

# Import SQL file
mysql -u root -p msp_realestate < sqlite_backup.sql
```

### Step 4: Switch Configuration

Edit `database.properties`:
```properties
DB_TYPE=mysql
MYSQL_USER=root
MYSQL_PASSWORD=yourpassword
```

### Step 5: Test Connection

```bash
mvn clean package
java -jar target/msp-realestate-1.0-SNAPSHOT.jar
```

---

## Database Schema

Both SQLite and MySQL use the same schema:

### Users Table
```sql
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone VARCHAR(20),
  role VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Properties Table
```sql
CREATE TABLE properties (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  type VARCHAR(100) NOT NULL,
  location VARCHAR(255) NOT NULL,
  area DECIMAL(10, 2) NOT NULL,
  price DECIMAL(15, 2) NOT NULL,
  status VARCHAR(50) NOT NULL,
  description TEXT,
  owner_name VARCHAR(255),
  owner_contact VARCHAR(20),
  created_by INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY(created_by) REFERENCES users(id)
);
```

### Documents Table
```sql
CREATE TABLE documents (
  id INT AUTO_INCREMENT PRIMARY KEY,
  property_id INT,
  doc_type VARCHAR(100) NOT NULL,
  doc_name VARCHAR(255) NOT NULL,
  doc_path VARCHAR(500) NOT NULL,
  uploaded_by INT,
  uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY(property_id) REFERENCES properties(id),
  FOREIGN KEY(uploaded_by) REFERENCES users(id)
);
```

---

## Docker Setup (Easiest for Local Development)

### Windows (PowerShell)

```powershell
# Pull and run MySQL
docker run -d --name mysql-server `
  -p 3306:3306 `
  -e MYSQL_ROOT_PASSWORD=mysql123 `
  -e MYSQL_DATABASE=msp_realestate `
  mysql:8.0

# Verify running
docker ps

# View logs
docker logs mysql-server
```

### Linux/macOS

```bash
# Pull and run MySQL
docker run -d --name mysql-server \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=mysql123 \
  -e MYSQL_DATABASE=msp_realestate \
  mysql:8.0

# Verify running
docker ps

# View logs
docker logs mysql-server
```

### Stop MySQL Container

```bash
docker stop mysql-server
docker rm mysql-server    # To completely remove
```

---

## Troubleshooting

### Issue: "Connection refused"
**Solution:**
1. Verify MySQL is running: `mysql --version`
2. Verify port 3306 is open: Check firewall settings
3. Verify credentials in `database.properties`

### Issue: "Unknown database 'msp_realestate'"
**Solution:**
```bash
mysql -u root -p -e "CREATE DATABASE msp_realestate;"
```

### Issue: "Access denied for user 'root'@'localhost'"
**Solution:**
1. Verify MySQL password in `database.properties`
2. Reset MySQL password if forgotten

### Issue: "Driver not found"
**Solution:**
```bash
# Rebuild project to download MySQL JDBC driver
mvn clean package
```

### Issue: "Tables not created"
**Solution:**
The application automatically creates tables on first run. Check console for errors:
```bash
java -jar target/msp-realestate-1.0-SNAPSHOT.jar 2>&1 | grep -i "error"
```

---

## Performance Comparison

| Aspect | SQLite | MySQL |
|--------|--------|-------|
| **Setup** | No setup | Requires server |
| **Local Development** | ✅ Best | ⚠ Extra steps |
| **Multi-user** | ⚠ Limited | ✅ Excellent |
| **Concurrent Access** | ⚠ Locks | ✅ Optimized |
| **Scalability** | ⚠ Limited | ✅ Scales well |
| **Cloud Deployment** | ⚠ Difficult | ✅ Easy (RDS, etc) |
| **Data Size** | ✅ Suitable | ✅ Any size |

**Recommendation:**
- **Development:** SQLite (default)
- **Production:** MySQL (scalability)

---

## Code Changes

### New File: DatabaseManagerV2.java

Enhanced database manager with dual-backend support:
- Automatic database type detection
- Configuration file support
- Both SQLite and MySQL schema initialization
- Connection management for both backends

### Updated: pom.xml

Added MySQL JDBC dependency:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

### New File: database.properties

Configuration file for database settings:
```properties
DB_TYPE=sqlite  # Change to mysql to use MySQL
MYSQL_HOST=localhost
MYSQL_PORT=3306
...
```

---

## Switching Between Databases

### SQLite (Current)
```properties
DB_TYPE=sqlite
```

### MySQL
```properties
DB_TYPE=mysql
MYSQL_PASSWORD=yourpassword
```

**No code changes required!** Just edit `database.properties` and rebuild.

---

## Next Steps

1. **Choose your database:**
   - Stay with SQLite for local development
   - Switch to MySQL for production

2. **If using MySQL:**
   - Install MySQL or Docker
   - Edit `database.properties`
   - Rebuild project: `mvn clean package`
   - Run application

3. **Monitor:**
   - Check console for connection status
   - Verify tables created in MySQL

---

## Support Resources

- **MySQL Documentation:** https://dev.mysql.com/doc/
- **MySQL JDBC Driver:** https://dev.mysql.com/downloads/connector/j/
- **Docker Hub MySQL:** https://hub.docker.com/_/mysql

---

## Version Information

- **Application:** MSP Real Estate System v1.0
- **Java:** 21 LTS
- **SQLite:** 3.42.0.0
- **MySQL JDBC:** 8.0.33
- **Date:** November 24, 2025

---

**Status:** ✅ **MySQL Integration Complete**

Your application now supports both SQLite (default) and MySQL databases.

Choose your database in `database.properties` and restart!
