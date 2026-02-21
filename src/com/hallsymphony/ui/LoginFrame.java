package com.hallsymphony.ui;

import com.hallsymphony.model.Role;
import com.hallsymphony.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final AppContext context;

    public LoginFrame(AppContext context) {
        this.context = context;
        setTitle("Hall Symphony - Login");
        setSize(420, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton regBtn = new JButton("Register Customer");

        setLayout(new GridLayout(4, 2, 8, 8));
        add(new JLabel("Username:")); add(userField);
        add(new JLabel("Password:")); add(passField);
        add(loginBtn); add(regBtn);

        loginBtn.addActionListener(e -> {
            context.authService.login(userField.getText().trim(), new String(passField.getPassword()).trim())
                .ifPresentOrElse(this::openRoleFrame, () -> JOptionPane.showMessageDialog(this, "Invalid credentials or blocked user."));
        });

        regBtn.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Username:");
            String password = JOptionPane.showInputDialog(this, "Password:");
            String fullName = JOptionPane.showInputDialog(this, "Full Name:");
            try {
                context.authService.registerCustomer(username, password, fullName);
                JOptionPane.showMessageDialog(this, "Registered successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }

    private void openRoleFrame(User user) {
        JFrame frame;
        if (user.getRole() == Role.CUSTOMER) frame = new CustomerFrame(context, user);
        else if (user.getRole() == Role.SCHEDULER) frame = new SchedulerFrame(context);
        else if (user.getRole() == Role.ADMINISTRATOR) frame = new AdminFrame(context);
        else frame = new ManagerFrame(context);

        frame.setVisible(true);
        dispose();
    }
}
