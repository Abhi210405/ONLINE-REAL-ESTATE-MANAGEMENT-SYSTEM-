package com.msp.realestate;

import javax.swing.SwingUtilities;

public class MSPRealEstateSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseManager.initializeDatabase();
            new LoginFrame().setVisible(true);
        });
    }
}

