package com.msp.realestate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PropertiesPanel extends JPanel {
    private User user;
    private JTable table;
    private DefaultTableModel model;

    public PropertiesPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(OrangeTheme.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header panel with orange theme
        JPanel header = OrangeTheme.createOrangeHeaderPanel("Property Management");
        add(header, BorderLayout.NORTH);

        // Table with property details
        model = new DefaultTableModel(
            new String[]{"ID", "Title", "Type", "Location", "Area (sqft)", "Price", "Status", "Owner"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new PropertyTableRenderer());
        table.setRowHeight(25);
        table.getTableHeader().setDefaultRenderer(OrangeTheme.getTableHeaderRenderer());

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(OrangeTheme.LIGHT_GRAY);
        
        JButton addBtn = OrangeTheme.createOrangeButton("Add Property");
        JButton viewBtn = OrangeTheme.createOrangeButton("View Details");
        JButton editBtn = OrangeTheme.createOrangeButton("Edit");
        JButton deleteBtn = OrangeTheme.createOrangeButton("Delete");
        JButton refreshBtn = OrangeTheme.createOrangeButton("Refresh");

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        // Button actions
        addBtn.addActionListener(e -> openAddPropertyDialog());
        viewBtn.addActionListener(e -> viewSelectedProperty());
        editBtn.addActionListener(e -> openEditPropertyDialog());
        deleteBtn.addActionListener(e -> deleteSelectedProperty());
        refreshBtn.addActionListener(e -> loadProperties());

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        loadProperties();
    }

    private void loadProperties() {
        model.setRowCount(0);
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM properties ORDER BY created_at DESC")) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("type"),
                    rs.getString("location"),
                    String.format("%.0f", rs.getDouble("area")),
                    String.format("$%.2f", rs.getDouble("price")),
                    rs.getString("status"),
                    rs.getString("owner_name")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading properties: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddPropertyDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Property", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Form fields
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Title:"), gbc);
        JTextField titleField = new JTextField(20);
        OrangeTheme.styleTextField(titleField);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Type:"), gbc);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Residential", "Commercial", "Industrial", "Land"});
        gbc.gridx = 1;
        panel.add(typeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Location:"), gbc);
        JTextField locationField = new JTextField(20);
        OrangeTheme.styleTextField(locationField);
        gbc.gridx = 1;
        panel.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Area (sqft):"), gbc);
        JTextField areaField = new JTextField(20);
        OrangeTheme.styleTextField(areaField);
        gbc.gridx = 1;
        panel.add(areaField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Price ($):"), gbc);
        JTextField priceField = new JTextField(20);
        OrangeTheme.styleTextField(priceField);
        gbc.gridx = 1;
        panel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Status:"), gbc);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Available", "Sold", "Rented", "Pending"});
        gbc.gridx = 1;
        panel.add(statusCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Description:"), gbc);
        JTextArea descArea = new JTextArea(3, 20);
        gbc.gridx = 1;
        panel.add(new JScrollPane(descArea), gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Owner Name:"), gbc);
        JTextField ownerField = new JTextField(20);
        OrangeTheme.styleTextField(ownerField);
        gbc.gridx = 1;
        panel.add(ownerField, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(new JLabel("Owner Contact:"), gbc);
        JTextField contactField = new JTextField(20);
        OrangeTheme.styleTextField(contactField);
        gbc.gridx = 1;
        panel.add(contactField, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveBtn = OrangeTheme.createOrangeButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        OrangeTheme.styleButton(cancelBtn);
        cancelBtn.setBackground(OrangeTheme.LIGHT_GRAY);

        saveBtn.addActionListener(e -> {
            try {
                double area = Double.parseDouble(areaField.getText());
                double price = Double.parseDouble(priceField.getText());
                
                String sql = "INSERT INTO properties (title, type, location, area, price, status, description, owner_name, owner_contact, created_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                try (Connection conn = DatabaseManager.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, titleField.getText());
                    pstmt.setString(2, (String) typeCombo.getSelectedItem());
                    pstmt.setString(3, locationField.getText());
                    pstmt.setDouble(4, area);
                    pstmt.setDouble(5, price);
                    pstmt.setString(6, (String) statusCombo.getSelectedItem());
                    pstmt.setString(7, descArea.getText());
                    pstmt.setString(8, ownerField.getText());
                    pstmt.setString(9, contactField.getText());
                    pstmt.setInt(10, user.id);
                    pstmt.executeUpdate();
                    
                    JOptionPane.showMessageDialog(dialog, "Property added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadProperties();
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for area and price", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }

    private void viewSelectedProperty() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a property to view", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int propertyId = (int) model.getValueAt(selectedRow, 0);
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM properties WHERE id = ?")) {
            pstmt.setInt(1, propertyId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String details = String.format(
                    "Property Details\n\n" +
                    "ID: %d\nTitle: %s\nType: %s\nLocation: %s\n" +
                    "Area: %.0f sqft\nPrice: $%.2f\nStatus: %s\n" +
                    "Description: %s\nOwner: %s\nContact: %s\n" +
                    "Created: %s",
                    rs.getInt("id"), rs.getString("title"), rs.getString("type"),
                    rs.getString("location"), rs.getDouble("area"), rs.getDouble("price"),
                    rs.getString("status"), rs.getString("description"),
                    rs.getString("owner_name"), rs.getString("owner_contact"),
                    rs.getString("created_at")
                );
                
                JOptionPane.showMessageDialog(this, details, "Property Details", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openEditPropertyDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a property to edit", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int propertyId = (int) model.getValueAt(selectedRow, 0);
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM properties WHERE id = ?")) {
            pstmt.setInt(1, propertyId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Property", true);
                dialog.setSize(500, 400);
                dialog.setLocationRelativeTo(this);

                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBackground(Color.WHITE);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);
                gbc.anchor = GridBagConstraints.WEST;

                JTextField titleField = new JTextField(rs.getString("title"), 20);
                OrangeTheme.styleTextField(titleField);
                JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Residential", "Commercial", "Industrial", "Land"});
                typeCombo.setSelectedItem(rs.getString("type"));
                JTextField locationField = new JTextField(rs.getString("location"), 20);
                OrangeTheme.styleTextField(locationField);
                JTextField areaField = new JTextField(String.valueOf(rs.getDouble("area")), 20);
                OrangeTheme.styleTextField(areaField);
                JTextField priceField = new JTextField(String.valueOf(rs.getDouble("price")), 20);
                OrangeTheme.styleTextField(priceField);
                JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Available", "Sold", "Rented", "Pending"});
                statusCombo.setSelectedItem(rs.getString("status"));
                JTextArea descArea = new JTextArea(rs.getString("description"), 3, 20);
                JTextField ownerField = new JTextField(rs.getString("owner_name"), 20);
                OrangeTheme.styleTextField(ownerField);

                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Title:"), gbc);
                gbc.gridx = 1;
                panel.add(titleField, gbc);
                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Type:"), gbc);
                gbc.gridx = 1;
                panel.add(typeCombo, gbc);
                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Location:"), gbc);
                gbc.gridx = 1;
                panel.add(locationField, gbc);
                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Area (sqft):"), gbc);
                gbc.gridx = 1;
                panel.add(areaField, gbc);
                gbc.gridx = 0; gbc.gridy = 4;
                panel.add(new JLabel("Price ($):"), gbc);
                gbc.gridx = 1;
                panel.add(priceField, gbc);
                gbc.gridx = 0; gbc.gridy = 5;
                panel.add(new JLabel("Status:"), gbc);
                gbc.gridx = 1;
                panel.add(statusCombo, gbc);
                gbc.gridx = 0; gbc.gridy = 6;
                panel.add(new JLabel("Description:"), gbc);
                gbc.gridx = 1;
                panel.add(new JScrollPane(descArea), gbc);
                gbc.gridx = 0; gbc.gridy = 7;
                panel.add(new JLabel("Owner Name:"), gbc);
                gbc.gridx = 1;
                panel.add(ownerField, gbc);

                JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
                JButton saveBtn = OrangeTheme.createOrangeButton("Save Changes");
                JButton cancelBtn = new JButton("Cancel");
                OrangeTheme.styleButton(cancelBtn);
                cancelBtn.setBackground(OrangeTheme.LIGHT_GRAY);

                saveBtn.addActionListener(e -> {
                    try {
                        double area = Double.parseDouble(areaField.getText());
                        double price = Double.parseDouble(priceField.getText());
                        
                        String updateSql = "UPDATE properties SET title=?, type=?, location=?, area=?, price=?, status=?, description=?, owner_name=? WHERE id=?";
                        
                        try (Connection updateConn = DatabaseManager.getConnection();
                             PreparedStatement updatePstmt = updateConn.prepareStatement(updateSql)) {
                            updatePstmt.setString(1, titleField.getText());
                            updatePstmt.setString(2, (String) typeCombo.getSelectedItem());
                            updatePstmt.setString(3, locationField.getText());
                            updatePstmt.setDouble(4, area);
                            updatePstmt.setDouble(5, price);
                            updatePstmt.setString(6, (String) statusCombo.getSelectedItem());
                            updatePstmt.setString(7, descArea.getText());
                            updatePstmt.setString(8, ownerField.getText());
                            updatePstmt.setInt(9, propertyId);
                            updatePstmt.executeUpdate();
                            
                            JOptionPane.showMessageDialog(dialog, "Property updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            loadProperties();
                            dialog.dispose();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Please enter valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                cancelBtn.addActionListener(e -> dialog.dispose());
                btnPanel.add(saveBtn);
                btnPanel.add(cancelBtn);

                gbc.gridx = 0; gbc.gridy = 8;
                gbc.gridwidth = 2;
                panel.add(btnPanel, gbc);

                dialog.add(new JScrollPane(panel));
                dialog.setVisible(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedProperty() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a property to delete", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int propertyId = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this property?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM properties WHERE id = ?")) {
                pstmt.setInt(1, propertyId);
                pstmt.executeUpdate();
                loadProperties();
                JOptionPane.showMessageDialog(this, "Property deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class PropertyTableRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                       boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (isSelected) {
                c.setBackground(OrangeTheme.LIGHT_ORANGE);
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(row % 2 == 0 ? Color.WHITE : OrangeTheme.PALE_ORANGE);
                c.setForeground(OrangeTheme.DARK_GRAY);
            }
            return c;
        }
    }
}
