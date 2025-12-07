package com.msp.realestate;

import java.sql.*;
import java.io.*;
import java.util.Properties;

/**
 * Enhanced DatabaseManager with support for both SQLite and MySQL
 * 
 * Configuration via database.properties:
 * - DB_TYPE: sqlite or mysql
 * - SQLite uses embedded database (msp_realestate.db)
 * - MySQL uses remote server connection
 * 
 * @author MSP Real Estate System
 * @version 2.0
 */
public class DatabaseManagerV2 {
    private static final String SQLITE_DB_URL = "jdbc:sqlite:msp_realestate.db";
    private static String dbType = "sqlite"; // default
    private static String mysqlHost = "localhost";
    private static int mysqlPort = 3306;
    private static String mysqlDatabase = "msp_realestate";
    private static String mysqlUser = "root";
    private static String mysqlPassword = "";
    // MSSQL configuration
    private static String mssqlHost = "localhost";
    private static int mssqlPort = 1433;
    private static String mssqlDatabase = "msp_realestate";
    private static String mssqlUser = "sa";
    private static String mssqlPassword = "";

    // Load configuration from database.properties
    static {
        loadConfiguration();
    }

    /**
     * Load database configuration from database.properties file
     */
    private static void loadConfiguration() {
        Properties props = new Properties();
        String configFile = "database.properties";
        
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream(configFile)) {
            if (input != null) {
                props.load(input);
                dbType = props.getProperty("DB_TYPE", "sqlite").toLowerCase();
                mysqlHost = props.getProperty("MYSQL_HOST", "localhost");
                mysqlPort = Integer.parseInt(props.getProperty("MYSQL_PORT", "3306"));
                mysqlDatabase = props.getProperty("MYSQL_DATABASE", "msp_realestate");
                mysqlUser = props.getProperty("MYSQL_USER", "root");
                mysqlPassword = props.getProperty("MYSQL_PASSWORD", "");
                mssqlHost = props.getProperty("MSSQL_HOST", "localhost");
                mssqlPort = Integer.parseInt(props.getProperty("MSSQL_PORT", "1433"));
                mssqlDatabase = props.getProperty("MSSQL_DATABASE", "msp_realestate");
                mssqlUser = props.getProperty("MSSQL_USER", "sa");
                mssqlPassword = props.getProperty("MSSQL_PASSWORD", "");
                
                System.out.println("✓ Database configuration loaded: DB_TYPE=" + dbType);
            } else {
                // Try file-based properties
                File configPath = new File("database.properties");
                if (configPath.exists()) {
                    try (FileInputStream fis = new FileInputStream(configPath)) {
                        props.load(fis);
                        dbType = props.getProperty("DB_TYPE", "sqlite").toLowerCase();
                        mysqlHost = props.getProperty("MYSQL_HOST", "localhost");
                        mysqlPort = Integer.parseInt(props.getProperty("MYSQL_PORT", "3306"));
                        mysqlDatabase = props.getProperty("MYSQL_DATABASE", "msp_realestate");
                        mysqlUser = props.getProperty("MYSQL_USER", "root");
                        mysqlPassword = props.getProperty("MYSQL_PASSWORD", "");
                        mssqlHost = props.getProperty("MSSQL_HOST", "localhost");
                        mssqlPort = Integer.parseInt(props.getProperty("MSSQL_PORT", "1433"));
                        mssqlDatabase = props.getProperty("MSSQL_DATABASE", "msp_realestate");
                        mssqlUser = props.getProperty("MSSQL_USER", "sa");
                        mssqlPassword = props.getProperty("MSSQL_PASSWORD", "");
                        
                        System.out.println("✓ Database configuration loaded from file: DB_TYPE=" + dbType);
                    }
                } else {
                    System.out.println("⚠ database.properties not found, using defaults (SQLite)");
                }
            }
        } catch (IOException e) {
            System.out.println("⚠ Error loading configuration: " + e.getMessage() + ", using defaults");
        }
    }

    /**
     * Get database connection based on configured type
     * @return Database connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if ("mysql".equalsIgnoreCase(dbType)) {
            try {
                return getMySQLConnection();
            } catch (SQLException myEx) {
                System.err.println("⚠ MySQL connection failed: " + myEx.getMessage());
                System.err.println("↪ Falling back to embedded SQLite database (sqlite)");
                return getSQLiteConnection();
            }
        } else if ("mssql".equalsIgnoreCase(dbType) || "sqlserver".equalsIgnoreCase(dbType)) {
            try {
                return getMSSQLConnection();
            } catch (SQLException msEx) {
                // SQL Server not reachable. Log a warning and fall back to SQLite.
                System.err.println("⚠ MSSQL connection failed: " + msEx.getMessage());
                System.err.println("↪ Falling back to embedded SQLite database (sqlite)");
                // Attempt SQLite instead of throwing the exception so the app can start with embedded DB.
                return getSQLiteConnection();
            }
        } else {
            return getSQLiteConnection();
        }
    }

    /**
     * Get SQLite connection (embedded database)
     * @return SQLite connection
     * @throws SQLException if connection fails
     */
    private static Connection getSQLiteConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found", e);
        }
        return DriverManager.getConnection(SQLITE_DB_URL);
    }

    /**
     * Get MySQL connection (remote server)
     * @return MySQL connection
     * @throws SQLException if connection fails
     */
    private static Connection getMySQLConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found", e);
        }
        
        String url = String.format(
            "jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
            mysqlHost, mysqlPort, mysqlDatabase
        );
        
        return DriverManager.getConnection(url, mysqlUser, mysqlPassword);
    }

    /**
     * Get MSSQL (Microsoft SQL Server) connection
     * @return MSSQL connection
     * @throws SQLException if connection fails
     */
    private static Connection getMSSQLConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MSSQL JDBC driver not found", e);
        }

        // Set a short login timeout to avoid long connection waits
        DriverManager.setLoginTimeout(5);

        String url = String.format("jdbc:sqlserver://%s:%d;databaseName=%s;encrypt=false;trustServerCertificate=true;loginTimeout=5",
            mssqlHost, mssqlPort, mssqlDatabase);

        return DriverManager.getConnection(url, mssqlUser, mssqlPassword);
    }

    /**
     * Get current database type
     * @return "sqlite" or "mysql"
     */
    public static String getDatabaseType() {
        return dbType;
    }

    /**
     * Get MySQL connection info
     * @return Connection info string
     */
    public static String getConnectionInfo() {
        if ("mysql".equalsIgnoreCase(dbType)) {
            return String.format("MySQL: %s@%s:%d/%s", mysqlUser, mysqlHost, mysqlPort, mysqlDatabase);
        } else if ("mssql".equalsIgnoreCase(dbType) || "sqlserver".equalsIgnoreCase(dbType)) {
            return String.format("MSSQL: %s@%s:%d/%s", mssqlUser, mssqlHost, mssqlPort, mssqlDatabase);
        } else {
            return "SQLite: " + SQLITE_DB_URL;
        }
    }

    /**
     * Initialize database schema (works for both SQLite and MySQL)
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Create users table
            String createUsersTable;
            if ("mysql".equalsIgnoreCase(dbType)) {
                createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(255) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "phone VARCHAR(20)," +
                    "role VARCHAR(50) NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB";
            } else if ("mssql".equalsIgnoreCase(dbType) || "sqlserver".equalsIgnoreCase(dbType)) {
                createUsersTable = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'users') CREATE TABLE users (" +
                    "id INT IDENTITY(1,1) PRIMARY KEY," +
                    "username NVARCHAR(255) UNIQUE NOT NULL," +
                    "password NVARCHAR(255) NOT NULL," +
                    "email NVARCHAR(255) NOT NULL," +
                    "phone NVARCHAR(20)," +
                    "role NVARCHAR(50) NOT NULL," +
                    "created_at DATETIME DEFAULT GETDATE()" +
                    ")";
            } else {
                createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "email TEXT NOT NULL," +
                    "phone TEXT," +
                    "role TEXT NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            }
            stmt.execute(createUsersTable);

            // Create properties table
            String createPropertiesTable;
            if ("mysql".equalsIgnoreCase(dbType)) {
                createPropertiesTable = "CREATE TABLE IF NOT EXISTS properties (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(255) NOT NULL," +
                    "type VARCHAR(100) NOT NULL," +
                    "location VARCHAR(255) NOT NULL," +
                    "area DECIMAL(10, 2) NOT NULL," +
                    "price DECIMAL(15, 2) NOT NULL," +
                    "status VARCHAR(50) NOT NULL," +
                    "description TEXT," +
                    "owner_name VARCHAR(255)," +
                    "owner_contact VARCHAR(20)," +
                    "created_by INT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(created_by) REFERENCES users(id)" +
                    ") ENGINE=InnoDB";
            } else if ("mssql".equalsIgnoreCase(dbType) || "sqlserver".equalsIgnoreCase(dbType)) {
                createPropertiesTable = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'properties') CREATE TABLE properties (" +
                    "id INT IDENTITY(1,1) PRIMARY KEY," +
                    "title NVARCHAR(255) NOT NULL," +
                    "type NVARCHAR(100) NOT NULL," +
                    "location NVARCHAR(255) NOT NULL," +
                    "area DECIMAL(10,2) NOT NULL," +
                    "price DECIMAL(15,2) NOT NULL," +
                    "status NVARCHAR(50) NOT NULL," +
                    "description NVARCHAR(MAX)," +
                    "owner_name NVARCHAR(255)," +
                    "owner_contact NVARCHAR(20)," +
                    "created_by INT," +
                    "created_at DATETIME DEFAULT GETDATE()," +
                    "FOREIGN KEY(created_by) REFERENCES users(id)" +
                    ")";
            } else {
                createPropertiesTable = "CREATE TABLE IF NOT EXISTS properties (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "type TEXT NOT NULL," +
                    "location TEXT NOT NULL," +
                    "area REAL NOT NULL," +
                    "price REAL NOT NULL," +
                    "status TEXT NOT NULL," +
                    "description TEXT," +
                    "owner_name TEXT," +
                    "owner_contact TEXT," +
                    "created_by INTEGER," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            }
            stmt.execute(createPropertiesTable);

            // Create documents table
            String createDocumentsTable;
            if ("mysql".equalsIgnoreCase(dbType)) {
                createDocumentsTable = "CREATE TABLE IF NOT EXISTS documents (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "property_id INT," +
                    "doc_type VARCHAR(100) NOT NULL," +
                    "doc_name VARCHAR(255) NOT NULL," +
                    "doc_path VARCHAR(500) NOT NULL," +
                    "uploaded_by INT," +
                    "uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(property_id) REFERENCES properties(id)," +
                    "FOREIGN KEY(uploaded_by) REFERENCES users(id)" +
                    ") ENGINE=InnoDB";
            } else if ("mssql".equalsIgnoreCase(dbType) || "sqlserver".equalsIgnoreCase(dbType)) {
                createDocumentsTable = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'documents') CREATE TABLE documents (" +
                    "id INT IDENTITY(1,1) PRIMARY KEY," +
                    "property_id INT," +
                    "doc_type NVARCHAR(100) NOT NULL," +
                    "doc_name NVARCHAR(255) NOT NULL," +
                    "doc_path NVARCHAR(500) NOT NULL," +
                    "uploaded_by INT," +
                    "uploaded_at DATETIME DEFAULT GETDATE()," +
                    "FOREIGN KEY(property_id) REFERENCES properties(id)," +
                    "FOREIGN KEY(uploaded_by) REFERENCES users(id)" +
                    ")";
            } else {
                createDocumentsTable = "CREATE TABLE IF NOT EXISTS documents (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "property_id INTEGER," +
                    "doc_type TEXT NOT NULL," +
                    "doc_name TEXT NOT NULL," +
                    "doc_path TEXT NOT NULL," +
                    "uploaded_by INTEGER," +
                    "uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(property_id) REFERENCES properties(id))";
            }
            stmt.execute(createDocumentsTable);

            // Insert default admin user if not exists
            String checkAdmin = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
            try (ResultSet rs = stmt.executeQuery(checkAdmin)) {
                if (rs.next() && rs.getInt(1) == 0) {
                        String insertAdmin;
                        if ("mssql".equalsIgnoreCase(dbType) || "sqlserver".equalsIgnoreCase(dbType)) {
                            insertAdmin = "INSERT INTO users (username, password, email, phone, role) " +
                                    "VALUES ('admin', 'admin123', 'admin@msp.com', '1234567890', 'Admin')";
                        } else {
                            insertAdmin = "INSERT INTO users (username, password, email, phone, role) " +
                                    "VALUES ('admin', 'admin123', 'admin@msp.com', '1234567890', 'Admin')";
                        }
                    stmt.execute(insertAdmin);
                    System.out.println("✓ Default admin user created (username: admin, password: admin123)");
                }
            }

            System.out.println("✓ Database schema initialized successfully");
            System.out.println("  Using: " + getConnectionInfo());

        } catch (SQLException e) {
            System.err.println("✗ Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
