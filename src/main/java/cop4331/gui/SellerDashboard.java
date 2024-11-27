package cop4331.gui;

import cop4331.server.Inventory;
import cop4331.server.Product;
import cop4331.server.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SellerDashboard {
    private JFrame frame;
    private Inventory inventory;

    public SellerDashboard(User user, Inventory inventory) {
        this.inventory = inventory;
        initialize(user);
    }

    private void initialize(User user) {
        frame = new JFrame("Seller Dashboard");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Header Label
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername() + "!", JLabel.CENTER);
        frame.add(welcomeLabel, BorderLayout.NORTH);

        // Inventory Panel
        JTextArea inventoryTextArea = new JTextArea();
        inventoryTextArea.setEditable(false);
        refreshInventoryDisplay(inventoryTextArea);

        JScrollPane scrollPane = new JScrollPane(inventoryTextArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Controls Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JButton addProductButton = new JButton("Add Product");
        JButton updateProductButton = new JButton("Update Product");
        JButton refreshButton = new JButton("Refresh Inventory");

        controlPanel.add(addProductButton);
        controlPanel.add(updateProductButton);
        controlPanel.add(refreshButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Action Listeners
        addProductButton.addActionListener(e -> addProduct());
        updateProductButton.addActionListener(e -> updateProduct());
        refreshButton.addActionListener(e -> refreshInventoryDisplay(inventoryTextArea));

        frame.setVisible(true);
    }

    private void addProduct() {
        String productId = JOptionPane.showInputDialog(frame, "Enter Product ID:");
        String name = JOptionPane.showInputDialog(frame, "Enter Product Name:");
        String type = JOptionPane.showInputDialog(frame, "Enter Product Type:");
        double invoicePrice = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter Invoice Price:"));
        double sellingPrice = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter Selling Price:"));
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Quantity:"));

        Product newProduct = new Product(productId, name, type, invoicePrice, sellingPrice, quantity);
        inventory.addProduct(newProduct);
    }

    private void updateProduct() {
        String productId = JOptionPane.showInputDialog(frame, "Enter Product ID to Update:");
        double newPrice = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter New Price:"));
        int newQuantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter New Quantity:"));

        inventory.updateProduct(productId, newPrice, newQuantity);
    }

    private void refreshInventoryDisplay(JTextArea inventoryTextArea) {
    inventoryTextArea.setText(""); // Clear existing text
    inventoryTextArea.append("Current Inventory:\n");
    for (Product product : inventory.getProducts()) {  // Using getProducts here
        inventoryTextArea.append(product.toString() + "\n");
    }
}

}
