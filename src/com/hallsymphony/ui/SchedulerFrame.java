package com.hallsymphony.ui;

import com.hallsymphony.model.Hall;
import com.hallsymphony.model.HallType;

import javax.swing.*;
import java.awt.*;

public class SchedulerFrame extends JFrame {
    public SchedulerFrame(AppContext context) {
        setTitle("Scheduler Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        JButton add = new JButton("Add Hall");
        JButton view = new JButton("View Halls");
        JButton delete = new JButton("Delete Hall");

        JPanel panel = new JPanel();
        panel.add(add); panel.add(view); panel.add(delete);
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);

        add.addActionListener(e -> {
            try {
                String name = JOptionPane.showInputDialog(this, "Hall name:");
                HallType type = HallType.valueOf(JOptionPane.showInputDialog(this, "Type: AUDITORIUM/BANQUET_HALL/MEETING_ROOM"));
                String remark = JOptionPane.showInputDialog(this, "Availability/Maintenance remarks:");
                context.hallService.create(name, type, remark);
                JOptionPane.showMessageDialog(this, "Hall created.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        view.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Hall h : context.hallService.findAll()) {
                sb.append(h.getId()).append(" | ").append(h.getName()).append(" | ").append(h.getType())
                        .append(" | ").append(h.getAvailabilityRemark()).append("\n");
            }
            area.setText(sb.toString());
        });

        delete.addActionListener(e -> {
            String hallId = JOptionPane.showInputDialog(this, "Hall ID:");
            context.hallService.delete(hallId);
            JOptionPane.showMessageDialog(this, "Hall deleted.");
        });
    }
}
