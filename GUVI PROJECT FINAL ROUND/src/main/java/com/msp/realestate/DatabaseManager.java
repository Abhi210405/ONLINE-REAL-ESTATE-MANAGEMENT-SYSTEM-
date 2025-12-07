package com.msp.realestate;

import java.sql.*;

public class DatabaseManager {
    // Delegate to DatabaseManagerV2 which supports SQLite, MySQL and MSSQL
    public static Connection getConnection() throws SQLException {
        return DatabaseManagerV2.getConnection();
    }

    public static void initializeDatabase() {
        DatabaseManagerV2.initializeDatabase();
    }
}
