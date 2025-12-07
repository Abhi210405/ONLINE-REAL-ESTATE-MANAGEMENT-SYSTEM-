package com.msp.realestate;

import java.sql.Connection;

/**
 * Small utility main for testing DB connections.
 * Usage: mvn exec:java -Dexec.mainClass=com.msp.realestate.DBHealthCheck
 */
public class DBHealthCheck {
    public static void main(String[] args) {
        try {
            System.out.println("Connection Info: " + DatabaseManagerV2.getConnectionInfo());
            try (Connection conn = DatabaseManagerV2.getConnection()) {
                System.out.println("Connection open: " + (conn != null && !conn.isClosed()));
            }
            System.out.println("DB health check succeeded.");
        } catch (Exception e) {
            System.err.println("DB health check failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
