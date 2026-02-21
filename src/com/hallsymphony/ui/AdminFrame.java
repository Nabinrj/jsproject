package com.hallsymphony.ui;

import com.hallsymphony.model.User;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {
    public AdminFrame(AppContext context) {
        setTitle("Administrator Dashboard");
        setSize(620, 420);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        JButton listUsers = new JButton("View Users");
        JButton blockUser = new JButton("Block User");
        JButton listBookings = new JButton("View All Bookings");

        JPanel panel = new JPanel();
        panel.add(listUsers); panel.add(blockUser); panel.add(listBookings);
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);

        listUsers.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (User u : context.userRepository.findAll()) {
                sb.append(u.getId()).append(" | ").append(u.getRole()).append(" | ").append(u.getUsername())
                        .append(" | blocked=").append(u.isBlocked()).append("\n");
            }
            area.setText(sb.toString());
        });

        blockUser.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Username to block:");
            context.userRepository.findByUsername(username).ifPresent(u -> {
                u.setBlocked(true);
                context.userRepository.save(u);
            });
            JOptionPane.showMessageDialog(this, "User updated if found.");
        });

        listBookings.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            context.bookingRepository.findAll().forEach(b -> sb.append(b.getId()).append(" | ")
                    .append(b.getCustomerId()).append(" | ").append(b.getHallId()).append(" | ").append(b.getStart()).append("\n"));
            area.setText(sb.toString());
        });
    }
}
