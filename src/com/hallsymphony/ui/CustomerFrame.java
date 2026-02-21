package com.hallsymphony.ui;

import com.hallsymphony.model.Booking;
import com.hallsymphony.model.Hall;
import com.hallsymphony.model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerFrame extends JFrame {
    public CustomerFrame(AppContext context, User customer) {
        setTitle("Customer Dashboard - " + customer.getFullName());
        setSize(600, 400);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        JButton listHalls = new JButton("View Halls");
        JButton book = new JButton("Book Hall");
        JButton myBookings = new JButton("My Bookings");

        JPanel panel = new JPanel();
        panel.add(listHalls); panel.add(book); panel.add(myBookings);
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);

        listHalls.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Hall h : context.bookingService.allHalls()) {
                sb.append(h.getId()).append(" | ").append(h.getName()).append(" | ").append(h.getType())
                        .append(" RM").append(h.getType().getHourlyRate()).append("/hour\n");
            }
            area.setText(sb.toString());
        });

        book.addActionListener(e -> {
            try {
                String hallId = JOptionPane.showInputDialog(this, "Hall ID:");
                LocalDateTime start = LocalDateTime.parse(JOptionPane.showInputDialog(this, "Start (YYYY-MM-DDTHH:MM):"));
                LocalDateTime end = LocalDateTime.parse(JOptionPane.showInputDialog(this, "End (YYYY-MM-DDTHH:MM):"));
                Booking booking = context.bookingService.book(customer.getId(), hallId, start, end);
                JOptionPane.showMessageDialog(this, "Booked. Receipt ID: " + booking.getId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        myBookings.addActionListener(e -> {
            List<Booking> bookings = context.bookingService.allBookings().stream()
                    .filter(b -> b.getCustomerId().equals(customer.getId())).toList();
            StringBuilder sb = new StringBuilder();
            for (Booking b : bookings) {
                sb.append(b.getId()).append(" | hall ").append(b.getHallId()).append(" | ").append(b.getStart())
                        .append(" - ").append(b.getEnd()).append(" | cancelled=").append(b.isCancelled()).append("\n");
            }
            area.setText(sb.toString());
        });
    }
}
