package com.msp.realestate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        setTitle("MSP Real Estate - Login");
        setSize(450, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Apply orange theme
        OrangeTheme.applyTheme(this);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(OrangeTheme.LIGHT_GRAY);
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel with orange background
        JPanel header = OrangeTheme.createOrangeHeaderPanel("MSP Real Estate System");
        main.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(OrangeTheme.WHITE);
        form.setBorder(BorderFactory.createLineBorder(OrangeTheme.LIGHT_ORANGE, 2));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userLabel.setForeground(OrangeTheme.TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(userLabel, gbc);

        userField = new JTextField(18);
        OrangeTheme.styleTextField(userField);
        gbc.gridx = 1; gbc.gridy = 0;
        form.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passLabel.setForeground(OrangeTheme.TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(passLabel, gbc);

        passField = new JPasswordField(18);
        passField.setBackground(OrangeTheme.WHITE);
        passField.setForeground(OrangeTheme.TEXT_COLOR);
        passField.setCaretColor(OrangeTheme.PRIMARY_ORANGE);
        passField.setBorder(BorderFactory.createLineBorder(OrangeTheme.LIGHT_ORANGE, 2));
        gbc.gridx = 1; gbc.gridy = 1;
        form.add(passField, gbc);

        JButton loginBtn = OrangeTheme.createOrangeButton("Login");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        form.add(loginBtn, gbc);

        JButton signupBtn = OrangeTheme.createOrangeButton("Create Account");
        signupBtn.setBackground(OrangeTheme.LIGHT_ORANGE);
        gbc.gridy = 3;
        form.add(signupBtn, gbc);

        loginBtn.addActionListener(e -> performLogin());
        signupBtn.addActionListener(e -> {
            new SignupFrame().setVisible(true);
            dispose();
        });

        main.add(form, BorderLayout.CENTER);
        add(main);
    }

    private void performLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("phone"), rs.getString("role"));
                new DashboardFrame(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
