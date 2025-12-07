package com.msp.realestate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DocumentsPanel extends JPanel {
    private User user;
    private JTable table;
    private DefaultTableModel model;

    public DocumentsPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Document Management");
        title.setFont(new Font("Arial", Font.BOLD, 20));

        model = new DefaultTableModel(new String[]{"ID", "Property ID", "Type", "Name", "Uploaded At", "Uploaded By"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
