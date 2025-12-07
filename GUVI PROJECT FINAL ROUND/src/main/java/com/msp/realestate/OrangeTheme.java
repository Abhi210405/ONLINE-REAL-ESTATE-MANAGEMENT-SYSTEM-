package com.msp.realestate;

import javax.swing.*;
import java.awt.*;

/**
 * Orange Theme Color Palette
 * Provides consistent orange-themed colors across the application
 */
public class OrangeTheme {
    // Primary Orange Colors
    public static final Color PRIMARY_ORANGE = new Color(255, 140, 0);      // Dark Orange
    public static final Color LIGHT_ORANGE = new Color(255, 165, 0);        // Orange
    public static final Color VERY_LIGHT_ORANGE = new Color(255, 200, 100); // Light Orange
    public static final Color PALE_ORANGE = new Color(255, 228, 181);       // Bisque
    
    // Supporting Colors
    public static final Color DARK_GRAY = new Color(51, 51, 51);            // Dark Gray
    public static final Color LIGHT_GRAY = new Color(240, 240, 240);        // Light Gray
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color TEXT_COLOR = new Color(51, 51, 51);           // Dark text
    
    // Status Colors
    public static final Color SUCCESS_GREEN = new Color(76, 175, 80);       // Green
    public static final Color ERROR_RED = new Color(244, 67, 54);           // Red
    public static final Color WARNING_YELLOW = new Color(255, 193, 7);      // Yellow
    
    /**
     * Apply orange theme to a frame
     */
    public static void applyTheme(JFrame frame) {
        frame.getContentPane().setBackground(LIGHT_GRAY);
    }
    
    /**
     * Style a button with orange theme
     */
    public static void styleButton(JButton button) {
        button.setBackground(PRIMARY_ORANGE);
        button.setForeground(WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Style a title label
     */
    public static void styleTitleLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(PRIMARY_ORANGE);
    }
    
    /**
     * Style a section header label
     */
    public static void styleHeaderLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(LIGHT_ORANGE);
    }
    
    /**
     * Style a text field with orange border
     */
    public static void styleTextField(JTextField field) {
        field.setBackground(WHITE);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(PRIMARY_ORANGE);
        field.setBorder(BorderFactory.createLineBorder(LIGHT_ORANGE, 2));
    }
    
    /**
     * Create a panel with orange header background
     */
    public static JPanel createOrangeHeaderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_ORANGE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        panel.add(titleLabel, BorderLayout.WEST);
        return panel;
    }
    
    /**
     * Create a styled orange button
     */
    public static JButton createOrangeButton(String text) {
        JButton button = new JButton(text);
        styleButton(button);
        return button;
    }
    
    /**
     * Create a table header renderer with orange theme
     */
    public static TableHeaderRenderer getTableHeaderRenderer() {
        return new TableHeaderRenderer();
    }
    
    /**
     * Custom table header renderer
     */
    public static class TableHeaderRenderer extends javax.swing.table.DefaultTableCellRenderer {
        public TableHeaderRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(PRIMARY_ORANGE);
            setForeground(WHITE);
            setFont(new Font("Arial", Font.BOLD, 12));
            setHorizontalAlignment(CENTER);
            return this;
        }
    }
}
