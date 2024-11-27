/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cop4331.gui;

import cop4331.client.ShoppingCart;
import cop4331.server.Inventory;
import cop4331.server.Product;
import cop4331.server.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerDashboard {
    private JFrame frame;
    private ShoppingCart cart;
    private Inventory inventory;

    public CustomerDashboard(User user, Inventory inventory) {
        this.cart = new ShoppingCart();
        this.inventory = inventory;
        initialize(user);
    }

    private void initialize(User user) {
        frame = new JFrame("Customer Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Header
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername() + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(welcomeLabel, BorderLayout.NORTH);

        // Product Display Area
        JTextArea productTextArea = new JTextArea();
        productTextArea.setEditable(false);
        refreshProductDisplay(productTextArea);

        JScrollPane scrollPane = new JScrollPane(productTextArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Control Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton viewProductDetailsButton = new JButton("View Product Details");
        JButton addToCartButton = new JButton("Add to Cart");
        JButton viewCartButton = new JButton("View Cart");
        JButton checkoutButton = new JButton("Checkout");

        buttonPanel.add(viewProductDetailsButton);
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners for Buttons
        viewProductDetailsButton.addActionListener(e -> viewProductDetails());
        addToCartButton.addActionListener(e -> addToCart());
        viewCartButton.addActionListener(e -> viewCart());
        checkoutButton.addActionListener(e -> checkout());

        frame.setVisible(true);
    }

    // Refreshes the product list
    private void refreshProductDisplay(JTextArea productTextArea) {
        productTextArea.setText(""); // Clear existing content
        productTextArea.append("Available Products:\n");
        productTextArea.append("------------------------------------------------------------\n");
        for (Product product : inventory.getProducts()) {
            productTextArea.append(product.toString() + "\n");
        }
    }

    // Displays product details for a specific product
    private void viewProductDetails() {
        String productId = JOptionPane.showInputDialog(frame, "Enter Product ID to View Details:");
        if (productId == null || productId.isEmpty()) return;

        Product product = inventory.getProduct(productId);
        if (product != null) {
            JOptionPane.showMessageDialog(frame, "Product Details:\n" +
                    "Name: " + product.getName() + "\n" +
                    "Type: " + product.getType() + "\n" +
                    "Price: $" + product.getSellingPrice() + "\n" +
                    "Available Quantity: " + product.getAvailableQuantity());
        } else {
            JOptionPane.showMessageDialog(frame, "Product not found.");
        }
    }

    // Adds a product to the shopping cart
    private void addToCart() {
        String productId = JOptionPane.showInputDialog(frame, "Enter Product ID to Add to Cart:");
        if (productId == null || productId.isEmpty()) return;

        String quantityString = JOptionPane.showInputDialog(frame, "Enter Quantity:");
        if (quantityString == null || quantityString.isEmpty()) return;

        int quantity;
        try {
            quantity = Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid quantity!");
            return;
        }

        Product product = inventory.getProduct(productId);
        if (product != null) {
            cart.addProduct(product, quantity);
            JOptionPane.showMessageDialog(frame, quantity + " x " + product.getName() + " added to cart.");
        } else {
            JOptionPane.showMessageDialog(frame, "Product not found.");
        }
    }

    // Displays the shopping cart contents
    private void viewCart() {
        StringBuilder cartDetails = new StringBuilder("Shopping Cart:\n");
        cart.getCartItems().forEach((productId, quantity) -> {
            Product product = inventory.getProduct(productId);
            if (product != null) {
                cartDetails.append(product.getName()).append(": ").append(quantity).append("\n");
            }
        });
        cartDetails.append("Total Cost: $").append(cart.getTotalCost());

        JOptionPane.showMessageDialog(frame, cartDetails.toString());
    }

    // Completes the checkout process
    private void checkout() {
        if (cart.getCartItems().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty!");
            return;
        }

        JOptionPane.showMessageDialog(frame, "Checkout complete! Total: $" + cart.getTotalCost());
        cart.clearCart(); // Clear the cart after checkout
    }
}
