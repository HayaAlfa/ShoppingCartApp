package cop4331.gui;

import cop4331.server.Inventory;
import cop4331.server.Product;
import cop4331.server.User;

import javax.swing.*;
import java.awt.*;

public class SellerDashboard {
    private JFrame frame;
    private Inventory inventory;

    public SellerDashboard(User seller, Inventory inventory) {
        this.inventory = inventory;
        initialize(seller);
    }

    private void initialize(User seller) {
        frame = new JFrame("Seller Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Header
        JLabel welcomeLabel = new JLabel("Welcome, " + seller.getUsername() + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(welcomeLabel, BorderLayout.NORTH);

        // Inventory Display
        JTextArea inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        refreshInventoryDisplay(inventoryArea);

        JScrollPane scrollPane = new JScrollPane(inventoryArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Controls
        JPanel controlPanel = new JPanel();
        JButton addProductButton = new JButton("Add Product");
        JButton updateProductButton = new JButton("Update Product");

        controlPanel.add(addProductButton);
        controlPanel.add(updateProductButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Add Product Button Action
        addProductButton.addActionListener(e -> addProduct(inventoryArea));

        // Update Product Button Action
        updateProductButton.addActionListener(e -> updateProduct(inventoryArea));

        frame.setVisible(true);
    }

    private void refreshInventoryDisplay(JTextArea inventoryArea) {
        inventoryArea.setText("Current Inventory:\n");
        for (Product product : inventory.getProducts()) {
            inventoryArea.append(product.toString() + "\n");
        }
    }

    private void addProduct(JTextArea inventoryArea) {
        String productId = JOptionPane.showInputDialog(frame, "Enter Product ID:");
        String name = JOptionPane.showInputDialog(frame, "Enter Product Name:");
        String type = JOptionPane.showInputDialog(frame, "Enter Product Type:");
        double sellingPrice = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter Selling Price:"));
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Quantity:"));
        String imagePath = JOptionPane.showInputDialog(frame, "Enter Image Path:");

        Product product = new Product(productId, name, type, 0, sellingPrice, quantity, imagePath);
        inventory.addProduct(product);

        refreshInventoryDisplay(inventoryArea);
    }

    private void updateProduct(JTextArea inventoryArea) {
        String productId = JOptionPane.showInputDialog(frame, "Enter Product ID to Update:");
        double newPrice = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter New Price:"));
        int newQuantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter New Quantity:"));

        Product product = inventory.getProduct(productId);
        if (product != null) {
            product.setSellingPrice(newPrice);
            product.setAvailableQuantity(newQuantity);
            refreshInventoryDisplay(inventoryArea);
        } else {
            JOptionPane.showMessageDialog(frame, "Product not found!");
        }
    }
}
