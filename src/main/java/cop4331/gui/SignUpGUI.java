package cop4331.gui;

import cop4331.server.LoginManager;
import cop4331.server.Inventory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JRadioButton sellerButton;
    private JRadioButton customerButton;
    private JLabel messageLabel;
    private LoginManager loginManager;
    private Inventory inventory;

    public SignUpGUI(LoginManager loginManager, Inventory inventory) {
        this.loginManager = loginManager;
        this.inventory = inventory;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Sign Up");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField();

        JLabel roleLabel = new JLabel("Select Role:");
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sellerButton = new JRadioButton("Seller");
        customerButton = new JRadioButton("Customer", true); // Default selection
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(sellerButton);
        roleGroup.add(customerButton);
        rolePanel.add(sellerButton);
        rolePanel.add(customerButton);

        JButton signUpButton = new JButton("Sign Up");
        JButton loginButton = new JButton("Login");

        messageLabel = new JLabel("", JLabel.CENTER);

        // Add components to panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);
        panel.add(roleLabel);
        panel.add(rolePanel);
        panel.add(new JLabel()); // Empty placeholder
        panel.add(signUpButton);
        panel.add(new JLabel()); // Empty placeholder
        panel.add(loginButton);

        // Add panel and message label to frame
        frame.add(panel, BorderLayout.CENTER);
        frame.add(messageLabel, BorderLayout.SOUTH);

        // Add action listener for sign-up button
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginGUI(loginManager, inventory);
            }
        });

        frame.setVisible(true);
    }

    private void handleSignUp() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username and password cannot be empty.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        boolean isSeller = sellerButton.isSelected();
        boolean isRegistered = loginManager.register(username, password, isSeller);
        if (isRegistered) {
            messageLabel.setText("Sign up successful! Please log in.");
            // Popup showing successful sign-up
            JOptionPane.showMessageDialog(frame, "Sign up successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose(); // Close the sign-up window
            new LoginGUI(loginManager, inventory); // Redirect to LoginGUI (pass inventory as needed)
        } else {
            messageLabel.setText("Username already exists. Try a different one.");
        }
    }
}
