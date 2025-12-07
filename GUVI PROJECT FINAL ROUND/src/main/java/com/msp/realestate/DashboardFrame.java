package com.msp.realestate;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private User user;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public DashboardFrame(User user) {
        this.user = user;
        setTitle("MSP Real Estate - Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        OrangeTheme.applyTheme(this);

        // Orange top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(OrangeTheme.PRIMARY_ORANGE);
        topBar.setPreferredSize(new Dimension(1100, 70));
        
        JLabel title = new JLabel("🏠 MSP REAL ESTATE SYSTEM");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(OrangeTheme.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topBar.add(title, BorderLayout.WEST);
        
        JLabel userInfo = new JLabel("User: " + user.username + " | Role: " + user.role);
        userInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        userInfo.setForeground(OrangeTheme.WHITE);
        userInfo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topBar.add(userInfo, BorderLayout.EAST);

        main.add(topBar, BorderLayout.NORTH);

        // Orange sidebar
        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setPreferredSize(new Dimension(220, 500));
        sideMenu.setBackground(OrangeTheme.VERY_LIGHT_ORANGE);
        sideMenu.setBorder(BorderFactory.createLineBorder(OrangeTheme.LIGHT_ORANGE, 2));

        String[] menuItems = {"Dashboard", "Properties", "Documents", "Users"};

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(OrangeTheme.LIGHT_GRAY);
        contentPanel.add(new DashboardPanel(user), "Dashboard");
        contentPanel.add(new PropertiesPanel(user), "Properties");
        contentPanel.add(new DocumentsPanel(user), "Documents");
        contentPanel.add(new UsersPanel(), "Users");

        for (String item : menuItems) {
            if (item.equals("Users") && user.role.equals("User")) continue;
            JButton btn = new JButton(item);
            btn.setMaximumSize(new Dimension(200, 50));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setBackground(OrangeTheme.LIGHT_ORANGE);
            btn.setForeground(OrangeTheme.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(OrangeTheme.PRIMARY_ORANGE);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(OrangeTheme.LIGHT_ORANGE);
                }
            });
            
            btn.addActionListener(e -> cardLayout.show(contentPanel, item));
            sideMenu.add(btn);
            sideMenu.add(Box.createVerticalStrut(8));
        }

        main.add(topBar, BorderLayout.NORTH);
        main.add(sideMenu, BorderLayout.WEST);
        main.add(contentPanel, BorderLayout.CENTER);

        add(main);
    }
}
