package com.hallsymphony.ui;

import com.hallsymphony.model.Issue;
import com.hallsymphony.model.IssueStatus;

import javax.swing.*;
import java.awt.*;

public class ManagerFrame extends JFrame {
    public ManagerFrame(AppContext context) {
        setTitle("Manager Dashboard");
        setSize(640, 420);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        JButton sales = new JButton("View Total Sales");
        JButton issues = new JButton("View Issues");
        JButton updateIssue = new JButton("Assign / Update Issue");

        JPanel panel = new JPanel();
        panel.add(sales); panel.add(issues); panel.add(updateIssue);
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);

        sales.addActionListener(e -> area.setText("Total sales (all non-cancelled bookings): RM " + context.salesService.totalSales()));

        issues.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Issue i : context.issueService.findAll()) {
                sb.append(i.getId()).append(" | booking=").append(i.getBookingId()).append(" | ").append(i.getDescription())
                        .append(" | ").append(i.getStatus()).append(" | scheduler=").append(i.getAssignedSchedulerId()).append("\n");
            }
            area.setText(sb.toString());
        });

        updateIssue.addActionListener(e -> {
            try {
                String issueId = JOptionPane.showInputDialog(this, "Issue ID:");
                String schedulerId = JOptionPane.showInputDialog(this, "Scheduler ID:");
                IssueStatus status = IssueStatus.valueOf(JOptionPane.showInputDialog(this, "Status: IN_PROGRESS/DONE/CLOSED/CANCELLED"));
                context.issueService.findAll().stream().filter(i -> i.getId().equals(issueId)).findFirst().ifPresent(i -> {
                    context.issueService.assignAndUpdate(i, schedulerId, status);
                });
                JOptionPane.showMessageDialog(this, "Issue updated if found.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }
}
