package com.msp.realestate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsersPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public UsersPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("User Management (Admin Only)");
        title.setFont(new Font("Arial", Font.BOLD, 20));

        model = new DefaultTableModel(new String[]{"ID", "Username", "Email", "Phone", "Role", "Created At"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
