/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cop4331.gui;

/**
 *
 * @author hayaalfakieh & leandro alfonso
 */

import cop4331.client.ShoppingCart;
import cop4331.server.Inventory;
import cop4331.server.Product;
import cop4331.server.User;
import cop4331.server.LoginManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomerDashboard {
    private JFrame frame;
    private Inventory inventory;
    private ShoppingCart cart;
    private JPanel productPanel;
    private JLabel totalLabel;
    private LoginManager loginManager;

    public CustomerDashboard(User customer, LoginManager loginManager, Inventory inventory) {
        this.inventory = inventory;
        this.loginManager = loginManager;
        this.cart = new ShoppingCart(inventory); // Pass inventory to ShoppingCart
        initialize(customer);
    }

    private void initialize(User customer) {
        frame = new JFrame("Customer Dashboard");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create a header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel welcomeLabel = new JLabel("Welcome, " + customer.getUsername() + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Create the logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                handleLogout();
            }
        });

        // Add the welcome label and logout button to the header panel
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Add the header panel to the frame
        frame.add(headerPanel, BorderLayout.NORTH);

        productPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(productPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        populateProducts();

        JPanel footerPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("TOTAL: $0.0", JLabel.LEFT);
        JButton viewCartButton = new JButton("View Cart");
        JButton checkoutButton = new JButton("Checkout");

        viewCartButton.addActionListener(e -> showCartScreen());
        checkoutButton.addActionListener(e -> showCheckoutPage());

        footerPanel.add(totalLabel, BorderLayout.WEST);
        footerPanel.add(viewCartButton, BorderLayout.CENTER);
        footerPanel.add(checkoutButton, BorderLayout.EAST);
        frame.add(footerPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void populateProducts() {
        productPanel.removeAll();

        List<Product> products = inventory.getProducts();
        for (Product product : products) {
            JPanel productCard = new JPanel(new BorderLayout());
            productCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel detailsLabel = new JLabel("<html>" +
                    "<b>Name:</b> " + product.getName() + "<br>" +
                    "<b>Price:</b> $" + product.getSellingPrice() + "<br>" +
                    "<b>Available:</b> " + product.getAvailableQuantity() + "<br>" +
                    "</html>");

            productCard.add(detailsLabel, BorderLayout.CENTER);

            productCard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showProductDetailsPopup(product);
                }
            });

            productPanel.add(productCard);
        }

        productPanel.revalidate();
        productPanel.repaint();
    }

    private void showCartScreen() {
        JDialog cartDialog = new JDialog(frame, "Shopping Cart", true);
        cartDialog.setSize(800, 500);
        cartDialog.setLayout(new BorderLayout());

        JPanel cartPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(cartPanel);

        cart.getCartItems().forEach((productId, quantity) -> {
            Product product = inventory.getProduct(productId);

            if (product != null) {
                JPanel cartItemPanel = new JPanel(new GridBagLayout());
                cartItemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 5, 5);

                // Product Image
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridheight = 2;
                JLabel imageLabel = new JLabel();
                ImageIcon productImage = new ImageIcon(
                    new ImageIcon(product.getImagePath()) // Ensure Product class has getImagePath() method
                        .getImage()
                        .getScaledInstance(50, 50, Image.SCALE_SMOOTH)
                );
                imageLabel.setIcon(productImage);
                cartItemPanel.add(imageLabel, gbc);

                // Product Details
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.gridheight = 1;
                gbc.anchor = GridBagConstraints.WEST;
                cartItemPanel.add(new JLabel("<html><b>" + product.getName() + "</b><br>" +
                        "Price: $" + product.getSellingPrice() + "</html>"), gbc);

                // Quantity Controls
                JPanel quantityPanel = new JPanel(new FlowLayout());
                JButton decrementButton = new JButton("-");
                JLabel quantityLabel = new JLabel(String.valueOf(quantity));
                JButton incrementButton = new JButton("+");

                // Action for Decrement
                decrementButton.addActionListener(e -> {
                    int currentQuantity = Integer.parseInt(quantityLabel.getText());
                    if (currentQuantity > 1) {
                        int newQuantity = currentQuantity - 1;

                        quantityLabel.setText(String.valueOf(newQuantity));
                        cart.updateProductQuantity(productId, newQuantity);
                        updateCartTotal(cartPanel);
                    }else {
                        JOptionPane.showMessageDialog(cartDialog, "Quantity cannot be less than 1.");
                    }
                });

                // Action for Increment
                incrementButton.addActionListener(e -> {
                    int currentQuantity = Integer.parseInt(quantityLabel.getText());
                    if (currentQuantity < product.getAvailableQuantity()) {
                        int newQuantity = currentQuantity + 1;
                        quantityLabel.setText(String.valueOf(newQuantity));
                        cart.updateProductQuantity(productId, newQuantity); 
                        updateCartTotal(cartPanel); 
                    } else {
                        JOptionPane.showMessageDialog(cartDialog,
                                "Only " + product.getAvailableQuantity() + " units available!");
                    }
                });

                quantityPanel.add(decrementButton);
                quantityPanel.add(quantityLabel);
                quantityPanel.add(incrementButton);

                gbc.gridx = 2;
                gbc.gridy = 0;
                cartItemPanel.add(quantityPanel, gbc);

                // Subtotal
                gbc.gridx = 3;
                gbc.gridy = 0;
                JLabel subtotalLabel = new JLabel("Subtotal: $" + (product.getSellingPrice() * quantity));
                cartItemPanel.add(subtotalLabel, gbc);

                // Remove Button
                gbc.gridx = 4;
                gbc.gridy = 0;
                JButton removeButton = new JButton("X");
                removeButton.addActionListener(e -> {
                    cart.removeProduct(product);
                    cartPanel.remove(cartItemPanel);
                    updateCartTotal(cartPanel);
                    cartPanel.revalidate();
                    cartPanel.repaint();
                });
                cartItemPanel.add(removeButton, gbc);

                cartPanel.add(cartItemPanel);
            }
        });

        cartDialog.add(scrollPane, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new BorderLayout());
        JLabel totalCartLabel = new JLabel("TOTAL: $" + cart.getTotalCost(), JLabel.LEFT);
        JButton saveButton = new JButton("Save Changes");
        JButton checkoutButton = new JButton("Proceed to Checkout");

        // Save Changes Listener
        saveButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(cartDialog, "All changes saved successfully!");
            cartDialog.dispose(); // Close the cart dialog
        });


        // Checkout Listener
        checkoutButton.addActionListener(e -> {
            cartDialog.dispose();
            showCheckoutPage();
        });

        footerPanel.add(totalCartLabel, BorderLayout.WEST);
        footerPanel.add(saveButton, BorderLayout.CENTER);
        footerPanel.add(checkoutButton, BorderLayout.EAST);
        cartDialog.add(footerPanel, BorderLayout.SOUTH);

        cartDialog.setVisible(true);
    }

    private void showProductDetailsPopup(Product product) {
        JDialog productDialog = new JDialog(frame, "Product Details", true);
        productDialog.setSize(400, 300);
        productDialog.setLayout(new BorderLayout());

        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
        detailsPanel.add(new JLabel("Name: " + product.getName()));
        detailsPanel.add(new JLabel("Price: $" + product.getSellingPrice()));
        detailsPanel.add(new JLabel("Available Quantity: " + product.getAvailableQuantity()));

        productDialog.add(detailsPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout());
        JLabel quantityLabel = new JLabel("Quantity:");
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, product.getAvailableQuantity(), 1));
        JButton addToCartButton = new JButton("Add to Cart");

        addToCartButton.addActionListener(e -> {
            int requestedQuantity = (int) quantitySpinner.getValue();

            if (requestedQuantity > product.getAvailableQuantity()) {
                JOptionPane.showMessageDialog(productDialog, "Error: Not enough stock available for " + product.getName() + "!");
            } else {
                cart.addProduct(product, requestedQuantity);
                JOptionPane.showMessageDialog(productDialog, "Added " + requestedQuantity + " x " + product.getName() + " to the cart!");
                productDialog.dispose();
                updateTotal();
            }
        });

        actionPanel.add(quantityLabel);
        actionPanel.add(quantitySpinner);
        actionPanel.add(addToCartButton);
        productDialog.add(actionPanel, BorderLayout.SOUTH);

        productDialog.setVisible(true);
    }

    private void updateTotal() {
        totalLabel.setText("TOTAL: $" + cart.getTotalCost());
    }

    private void updateCartTotal(JPanel cartPanel) {
        totalLabel.setText("TOTAL: $" + cart.getTotalCost());
        cartPanel.revalidate();
        cartPanel.repaint();
    }
    
    private void updateInventory() {
        // Loop through all items in the cart and update the inventory
        cart.getCartItems().forEach((productId, quantity) -> {
            Product product = inventory.getProduct(productId);
            if (product != null) {
                product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
            }
        });
    }
    
    private void showCheckoutPage() {
        JDialog checkoutDialog = new JDialog(frame, "Checkout", true);
        checkoutDialog.setSize(600, 500);
        checkoutDialog.setLayout(new BorderLayout());

        // Cart Contents Panel
        JPanel cartContentsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(cartContentsPanel);

        cart.getCartItems().forEach((productId, quantity) -> {
            Product product = inventory.getProduct(productId);

            if (product == null) {
                // Remove invalid product from cart
                cart.removeProduct(product);
            } else if (quantity <= product.getAvailableQuantity()) {
                JPanel cartItemPanel = new JPanel(new GridLayout(1, 3));
                cartItemPanel.add(new JLabel(product.getName()));
                cartItemPanel.add(new JLabel("Qty: " + quantity));
                cartItemPanel.add(new JLabel("Subtotal: $" + (product.getSellingPrice() * quantity)));

                cartContentsPanel.add(cartItemPanel);
            } else {
                JOptionPane.showMessageDialog(checkoutDialog, "There is not enough stock available for " + product.getName() + ". Please update the quantity in your cart.");
                checkoutDialog.dispose();
            }
        });

        checkoutDialog.add(scrollPane, BorderLayout.CENTER);

        // Payment Panel
        JPanel paymentPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment Information"));

        paymentPanel.add(new JLabel("Card Number:"));
        JTextField cardNumberField = new JTextField();
        paymentPanel.add(cardNumberField);

        paymentPanel.add(new JLabel("Expiry Date (MM/YY):"));
        JTextField expiryField = new JTextField();
        paymentPanel.add(expiryField);

        paymentPanel.add(new JLabel("CVV:"));
        JTextField cvvField = new JTextField();
        paymentPanel.add(cvvField);

        checkoutDialog.add(paymentPanel, BorderLayout.NORTH);

        // Footer with Total and Checkout Button
        JPanel footerPanel = new JPanel(new BorderLayout());
        JLabel totalLabel = new JLabel("TOTAL: $" + cart.getTotalCost());
        JButton confirmButton = new JButton("Confirm Purchase");

        confirmButton.addActionListener(e -> {
            if (processPayment(cardNumberField.getText(), expiryField.getText(), cvvField.getText())) {
                updateInventory();
                JOptionPane.showMessageDialog(checkoutDialog, "Purchase confirmed! Thank you.");
                cart.clearCart(); // Clear the cart after purchase
                updateTotal();
                checkoutDialog.dispose();
                populateProducts(); // Refresh product list
            } else {
                JOptionPane.showMessageDialog(checkoutDialog, "Payment failed. Please try again.");
            }
        });

        footerPanel.add(totalLabel, BorderLayout.WEST);
        footerPanel.add(confirmButton, BorderLayout.EAST);
        checkoutDialog.add(footerPanel, BorderLayout.SOUTH);

        checkoutDialog.setVisible(true);
    }
 
    private boolean processPayment(String cardNumber, String expiry, String cvv) {
        // Validate credit card fields
        if (cardNumber.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all payment details.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Simulate basic validation
        if (!cardNumber.matches("\\d{16}")) { // Ensure card number has 16 digits
            JOptionPane.showMessageDialog(frame, "Invalid card number. Please enter a valid 16-digit number.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!expiry.matches("\\d{2}/\\d{2}")) { // Ensure expiry date is in MM/YY format
            JOptionPane.showMessageDialog(frame, "Invalid expiry date. Please use MM/YY format.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!cvv.matches("\\d{3}")) { // Ensure CVV is a 3-digit number
            JOptionPane.showMessageDialog(frame, "Invalid CVV. Please enter a valid 3-digit number.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Simulate payment processing
        JOptionPane.showMessageDialog(frame, "Payment processed successfully!", "Payment Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    private void handleLogout() {
        frame.dispose(); // Close the login window
        new LoginGUI(loginManager, inventory);
    }
}



