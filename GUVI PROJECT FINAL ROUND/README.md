# MSP Real Estate System

[![Java CI — Maven](https://github.com/your-username/msp-realestate/actions/workflows/maven.yml/badge.svg)](https://github.com/your-username/msp-realestate/actions/workflows/maven.yml)
[![Java 21](https://img.shields.io/badge/Java-21%20LTS-blue)](https://www.oracle.com/java/technologies/downloads/#java21)
[![Maven](https://img.shields.io/badge/Maven-3.8.0+-brightgreen)](https://maven.apache.org/)

A modern Java Swing–based desktop application for managing real estate properties, users, and documents. This project is built with **Java 21 LTS**, Maven, and SQLite.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Requirements](#requirements)
- [Installation](#installation)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Features](#features)
- [Database](#database)
- [Troubleshooting](#troubleshooting)

---

## Project Overview

The **MSP Real Estate System** is a comprehensive desktop application designed to streamline real estate operations, including:

- **User Management:** Secure login, signup, and user role management.
- **Property Management:** Add, edit, view, and search properties.
- **Document Management:** Store and organize property-related documents.
- **Dashboard:** Real-time overview of key metrics and system status.

The application uses a **Graphical User Interface (GUI)** built with Java Swing and stores data locally in an **SQLite database**.

---

## Requirements

### System Requirements
- **Java:** Java 21 LTS or later
- **Maven:** 3.8.0 or later
- **Operating System:** Windows, macOS, or Linux
- **RAM:** Minimum 512 MB
- **Disk Space:** Minimum 100 MB

### Java 21 Installation

#### Windows

**Option 1: Eclipse Temurin (Recommended)**
1. Download from [Adoptium](https://adoptium.net/)
2. Run the installer and select "Add to PATH" during installation
3. Verify installation:
   ```bat
   java -version
   ```

**Option 2: Oracle JDK 21**
1. Download from [Oracle](https://www.oracle.com/java/technologies/downloads/#java21)
2. Run the installer
3. Set `JAVA_HOME` environment variable:
   - Right-click **This PC** → **Properties** → **Advanced system settings** → **Environment Variables**
   - Click **New** (under System variables) and add:
     - Variable name: `JAVA_HOME`
     - Variable value: `C:\Program Files\Java\jdk-21` (adjust path as needed)
4. Add Java to PATH:
   - In the same Environment Variables window, select `Path` → **Edit**
   - Add a new entry: `%JAVA_HOME%\bin`
5. Restart your command prompt and verify:
   ```bat
   java -version
   ```

#### macOS / Linux

Use a package manager or download from [Adoptium](https://adoptium.net/):

**macOS (Homebrew):**
```bash
brew install temurin21
java -version
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install temurin-21-jdk
java -version
```

---

## Installation

1. **Clone or download the project:**
   ```bat
   cd "C:\Users\Abhi Nandan Pandey\OneDrive\Desktop\GUVI PROJECT"
   ```

2. **Verify Java 21 is installed:**
   ```bat
   java -version
   ```
   Expected output: `openjdk version "21" ...` or `java version "21" ...`

3. **Verify Maven is installed:**
   ```bat
   mvn -version
   ```
   Expected output: `Maven 3.8.0+` and the JDK being used should show Java 21.

4. **If Maven is not installed**, download from [Apache Maven](https://maven.apache.org/download.cgi) and follow their installation guide.

---

## Building the Project

Navigate to the project root directory and run:

```bat
mvn clean package
```

This command will:
- Clean any previous builds (`clean`)
- Download dependencies
- Compile source code
- Run unit tests (if any)
- Package the application into a JAR file in the `target/` directory

**To skip tests during build:**
```bat
mvn -DskipTests clean package
```

**For creating installers (Windows MSI, macOS DMG, Linux DEB):**
See [INSTALLER.md](INSTALLER.md) for detailed packaging instructions.

---

## Running the Application

### Option 1: Using Maven Exec Plugin (Recommended)

```bat
mvn exec:java -Dexec.mainClass="com.msp.realestate.MSPRealEstateSystem"
```

### Option 2: From Your IDE

1. Open the project in IntelliJ IDEA, Eclipse, or NetBeans
2. Set the project JDK to **Java 21**
3. Locate `MSPRealEstateSystem.java` in `src/main/java/com/msp/realestate/`
4. Right-click and select **Run** (or press `Shift + F10` in IntelliJ)

### Option 3: Run Executable Fat JAR (Recommended for End Users)

After building with `mvn clean package`, the fat JAR includes all dependencies (SQLite JDBC, etc.):
```bat
java -jar target/msp-realestate-1.0-SNAPSHOT.jar
```

No additional classpath or Maven configuration needed — just run the single JAR file.

---

## Project Structure

```
GUVI PROJECT/
├── pom.xml                          # Maven project configuration (Java 21 target)
├── README.md                        # This file
├── src/
│   └── main/
│       ├── java/
│       │   └── com/msp/realestate/
│       │       ├── MSPRealEstateSystem.java       # Application entry point
│       │       ├── DashboardFrame.java            # Main window frame
│       │       ├── DashboardPanel.java            # Dashboard UI panel
│       │       ├── LoginFrame.java                # Login screen
│       │       ├── SignupFrame.java               # User registration screen
│       │       ├── PropertiesPanel.java           # Property management UI
│       │       ├── UsersPanel.java                # User management UI
│       │       ├── DocumentsPanel.java            # Document management UI
│       │       ├── DatabaseManager.java           # Database operations (SQLite)
│       │       └── User.java                      # User model class
│       └── resources/                             # Configuration and assets (if any)
├── target/                          # Build output (generated)
│   ├── classes/                     # Compiled .class files
│   ├── msp-realestate-1.0-SNAPSHOT.jar
│   └── ... (other Maven build artifacts)
└── backup/
    └── MSPRealEstateSystem-ORIGINAL.java          # Original single-file backup
```

---

## Features

### 1. User Management
- **Secure Login:** Authenticate with username and password
- **User Registration:** Create new user accounts
- **Role-based Access:** Different user roles with specific permissions
- **User Dashboard:** View and manage all users in the system

### 2. Property Management
- **Add Properties:** Create new property listings with details (address, price, type, etc.)
- **Edit Properties:** Update property information
- **View Properties:** Browse all properties with filtering and search
- **Delete Properties:** Remove properties from the system

### 3. Document Management
- **Upload Documents:** Store property-related documents (images, PDFs, contracts, etc.)
- **Organize Files:** Link documents to specific properties
- **Retrieve Documents:** Quick access to property documentation

### 4. Dashboard
- **Overview:** Real-time statistics and system metrics
- **Quick Actions:** Fast access to common tasks
- **Reports:** Property and user summaries

---

## Database

### Supported Databases

The application supports **two database backends**:

| Feature | SQLite | MySQL |
|---------|--------|-------|
| **Setup** | ✅ None | ⚠ Requires server |
| **Local Dev** | ✅ Best | ⚠ Extra config |
| **Multi-user** | ⚠ Limited | ✅ Excellent |
| **Production** | ⚠ Limited | ✅ Recommended |
| **Cloud** | ⚠ Difficult | ✅ Easy (RDS) |

### SQLite (Default)

The application uses **SQLite** by default for persistent data storage. The database file (`msp_realestate.db`) is created automatically in the project root on first run.

- **Configuration:** Edit `database.properties` (DB_TYPE=sqlite)
- **Location:** `msp_realestate.db` (project root)
- **No installation required** — Embedded database

### MySQL (Optional)

For scalable production deployments, switch to **MySQL**:

1. **Install MySQL** (or use Docker)
   ```bash
   docker run -d --name mysql-server \
     -p 3306:3306 \
     -e MYSQL_ROOT_PASSWORD=mysql123 \
     -e MYSQL_DATABASE=msp_realestate \
     mysql:8.0
   ```

2. **Edit `database.properties`:**
   ```properties
   DB_TYPE=mysql
   MYSQL_HOST=localhost
   MYSQL_PORT=3306
   MYSQL_USER=root
   MYSQL_PASSWORD=mysql123
   ```

3. **Rebuild and run:**
   ```bash
   mvn clean package
   java -jar target/msp-realestate-1.0-SNAPSHOT.jar
   ```

**For detailed MySQL setup:** See [MYSQL_INTEGRATION_GUIDE.md](MYSQL_INTEGRATION_GUIDE.md)

### Developer Quick Start (Windows PowerShell)

To start a ready dev MySQL instance and run the application quickly, use the included `run-dev.ps1` (requires Docker):

```powershell
# Run from project root. Adjust username/password as needed.
.\run-dev.ps1 -dbUser root -dbPassword Abhi@62348
```

The script will:
- Start a MySQL Docker container (mysql:8.0) if not present
- Generate `database.properties` with MySQL settings
- Build the project and copy runtime dependencies
- Run the `DBHealthCheck` tool to verify the connection

For macOS / Linux, use `run-dev.sh` with similar arguments.

### VS Code
Follow the earlier steps to install SQLTools and configure a MySQL connection. If `run-dev.ps1` is used, the connection details are: host `localhost`, port `3306`, DB `msp_realestate`, user `root`, password `Abhi@62348`.

### MSSQL (Microsoft SQL Server) (Optional)

You can also configure the application to use Microsoft SQL Server for a scalable production-ready database.

1. **Install SQL Server** (or use the official Docker image):
   ```powershell
   docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=YourStrong!Passw0rd" -p 1433:1433 --name sqlserver -d mcr.microsoft.com/mssql/server:2019-latest
   ```

2. **Edit `database.properties`:**
   ```properties
   DB_TYPE=mssql
   MSSQL_HOST=localhost
   MSSQL_PORT=1433
   MSSQL_DATABASE=msp_realestate
   MSSQL_USER=sa
   MSSQL_PASSWORD=YourStrong!Passw0rd
   ```

3. **Rebuild and run:**
   ```powershell
   mvn clean package
   java -jar target/msp-realestate-1.0-SNAPSHOT.jar
   ```

The application uses a Microsoft JDBC driver (mssql-jdbc); the dependency is already added to `pom.xml`.

### Resetting the Database

**SQLite:**
1. Close the application
2. Delete `msp_realestate.db` from the project root
3. Restart the application — a new database will be created

**MySQL:**
1. Drop and recreate the database:
   ```sql
   DROP DATABASE msp_realestate;
   CREATE DATABASE msp_realestate;
   ```
2. Restart the application — tables will be auto-created

### Database Tables

- `users` — User credentials and profiles
- `properties` — Real estate property listings
- `documents` — Associated documents and file metadata

---

## Troubleshooting

### Issue: "Java 21 not found" or version mismatch

**Solution:**
1. Verify Java 21 installation:
   ```bat
   java -version
   ```
2. Check `JAVA_HOME` environment variable:
   ```bat
   echo %JAVA_HOME%
   ```
3. If needed, reinstall Java 21 and set `JAVA_HOME` correctly.

### Issue: Maven build fails with "source/target version mismatch"

**Solution:**
- Ensure your IDE is using Java 21 as the project SDK
- Run:
  ```bat
  mvn clean package
  ```
- Check `pom.xml` confirms:
  ```xml
  <maven.compiler.source>21</maven.compiler.source>
  <maven.compiler.target>21</maven.compiler.target>
  ```

### Issue: Application won't start or GUI appears corrupted

**Solution:**
1. Ensure you're running on Java 21:
   ```bat
   mvn --version
   ```
2. Try rebuilding from scratch:
   ```bat
   mvn clean package
   ```
3. Check system RAM and available disk space

### Issue: Database file is locked or inaccessible

**Solution:**
1. Close the application completely
2. Ensure no other processes are accessing the database
3. On Windows, restart the command prompt if needed
4. Restart the application

### Issue: "Cannot find or load main class"

**Solution:**
- Verify the main class path in `pom.xml`:
  ```xml
  <mainClass>com.msp.realestate.MSPRealEstateSystem</mainClass>
  ```
- Rebuild and run:
  ```bat
  mvn clean package
  mvn exec:java
  ```

---

## Development

### IDE Setup (IntelliJ IDEA)

1. Open the project folder
2. IntelliJ should auto-detect the Maven project
3. Go to **File** → **Project Structure** → **Project**
4. Set **SDK** to **Java 21**
5. Set **Language level** to **21**
6. Click **Apply** and **OK**

### IDE Setup (Eclipse)

1. Import the project: **File** → **Import** → **Maven** → **Existing Maven Projects**
2. Select the project folder and finish
3. Right-click the project → **Properties** → **Project Facets**
4. Set **Java** to version **21**

### IDE Setup (NetBeans)

1. **File** → **Open Project** → Select the project folder
2. Right-click the project → **Properties** → **Run**
3. Ensure **JDK** is set to **Java 21**

---

## Support & Contribution

For issues, suggestions, or contributions, please:
1. Check existing documentation
2. Review the backup file if needed: `backup/MSPRealEstateSystem-ORIGINAL.java`
3. Test your changes locally before committing

---

## License

This project is proprietary and maintained by MSP Real Estate.

---

## Version History

- **v1.0** (Current) — Refactored to modular Java classes with Java 21 LTS
- **v0.9** — Original single-file implementation (backup available)

---

**Last Updated:** November 2025  
**Java Version:** 21 LTS  
**Build Tool:** Maven 3.8.0+  
**Package:** `target/msp-realestate-1.0-SNAPSHOT.jar` (executable fat JAR with all dependencies)  
**License:** Proprietary (MSP Real Estate)
