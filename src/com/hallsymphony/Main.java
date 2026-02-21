package com.hallsymphony;

import com.hallsymphony.ui.AppContext;
import com.hallsymphony.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppContext context = new AppContext();
        context.userRepository.bootstrapDefaults();

        SwingUtilities.invokeLater(() -> new LoginFrame(context).setVisible(true));
    }
}
