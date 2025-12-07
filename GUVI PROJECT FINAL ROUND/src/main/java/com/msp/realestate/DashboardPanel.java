package com.msp.realestate;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel(User user) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.add(new JLabel("Statistics will appear here."));
        add(center, BorderLayout.CENTER);
    }
}
