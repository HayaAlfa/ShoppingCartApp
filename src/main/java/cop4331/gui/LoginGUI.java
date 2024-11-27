/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cop4331.gui;

import cop4331.gui.CustomerDashboard;
import cop4331.server.Inventory;
import cop4331.server.LoginManager;
import cop4331.server.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private LoginManager loginManager;
    private Inventory inventory;

    public LoginGUI(LoginManager loginManager, Inventory inventory) {
        this.loginManager = loginManager;
        this.inventory = inventory;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        messageLabel = new JLabel("", JLabel.CENTER);

        // Add components to panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty placeholder
        panel.add(loginButton);

        // Add panel and message label to frame
        frame.add(panel, BorderLayout.CENTER);
        frame.add(messageLabel, BorderLayout.SOUTH);

        // Add action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        frame.setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User user = loginManager.login(username, password);
        if (user != null) {
            messageLabel.setText("Welcome, " + user.getUsername() + "!");
            frame.dispose(); // Close the login window

            if (user.isSeller()) {
                // Redirect to Seller Dashboard
                new SellerDashboard(user, inventory);
            } else {
                // Redirect to Customer Dashboard
                new CustomerDashboard(user, inventory);
            }
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }
}

