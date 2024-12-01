package cop4331.gui;

/**
 *
 * @author hayaalfakieh & leandro alfonso
 */

import cop4331.server.Inventory;
import cop4331.server.LoginManager;
import cop4331.server.Product;
import cop4331.server.User;

import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.awt.*;
import java.util.List;

public class SellerDashboard {
    private JFrame frame;
    private Inventory inventory;
    private JPanel inventoryPanel;
    private LoginManager loginManager;

    public SellerDashboard(User seller, LoginManager loginManager, Inventory inventory) {
        this.inventory = inventory;
        this.loginManager = loginManager;
        initialize(seller);
    }

    private void initialize(User seller) {
        frame = new JFrame("Seller Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create a header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel welcomeLabel = new JLabel("Welcome, " + seller.getUsername() + "!", JLabel.CENTER);
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

        // Inventory Display
        inventoryPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(inventoryPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        populateProducts();

        // Controls
        JPanel controlPanel = new JPanel();
        JButton addProductButton = new JButton("Add Product");

        controlPanel.add(addProductButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Add Product Button Action
        addProductButton.addActionListener(e -> showAddProductPopup());

        frame.setVisible(true);
    }

    private void showAddProductPopup() {
        JDialog productDialog = new JDialog(frame, "Add Product", true);
        productDialog.setSize(400, 300);
        productDialog.setLayout(new BorderLayout());
    
        JPanel addPanel = new JPanel(new GridLayout(6, 2, 10, 10));
    
        // Input Fields
        JTextField idField = new JTextField(); // Product ID
        JTextField nameField = new JTextField(); // Name
        JTextField typeField = new JTextField(); // Type
        JTextField priceField = new JTextField(); // Price
        JTextField quantityField = new JTextField(); // Quantity
        JTextField imagePathField = new JTextField(); // Image Path
    
        addPanel.add(new JLabel("Product ID:"));
        addPanel.add(idField);
        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Type:"));
        addPanel.add(typeField);
        addPanel.add(new JLabel("Price:"));
        addPanel.add(priceField);
        addPanel.add(new JLabel("Quantity:"));
        addPanel.add(quantityField);
        addPanel.add(new JLabel("Image Path:"));
        addPanel.add(imagePathField);
    
        productDialog.add(addPanel, BorderLayout.CENTER);
    
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
    
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
    
        productDialog.add(buttonPanel, BorderLayout.SOUTH);
    
        // Add Button Action
        addButton.addActionListener(e -> {
            try {
                String productId = idField.getText();
                String name = nameField.getText();
                String type = typeField.getText();
                double sellingPrice = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                String imagePath = imagePathField.getText();
    
                if (productId.isEmpty() || name.isEmpty() || type.isEmpty()) {
                    JOptionPane.showMessageDialog(productDialog, "Please fill out all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                Product newProduct = new Product(productId, name, type, 0, sellingPrice, quantity, imagePath);
                inventory.addProduct(newProduct);
    
                populateProducts(); // Refresh product list
                productDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(productDialog, "Invalid input. Please enter valid numbers for price and quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        // Cancel Button Action
        cancelButton.addActionListener(e -> productDialog.dispose());
    
        productDialog.setLocationRelativeTo(frame);
        productDialog.setVisible(true);
    }
    

    private void populateProducts() {
        inventoryPanel.removeAll();

        List<Product> products = inventory.getProducts();
        for (Product product : products) {
            JPanel productCard = new JPanel(new BorderLayout());
            productCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel detailsLabel = new JLabel("<html>" +
                    "<b>Product ID:</b> " + product.getProductId() + "<br>" +
                    "<b>Name:</b> " + product.getName() + "<br>" +
                    "<b>Type:</b> " + product.getType() + "<br>" +
                    "<b>Price:</b> $" + product.getSellingPrice() + "<br>" +
                    "<b>Available:</b> " + product.getAvailableQuantity() + "<br>" +
                    "</html>");

            productCard.add(detailsLabel, BorderLayout.CENTER);

            productCard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showEditPopup(product);
                }
            });

            inventoryPanel.add(productCard);
        }

        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }

    private void showEditPopup(Product product) {
        JDialog productDialog = new JDialog(frame, "Edit Product", true);
        productDialog.setSize(400, 300);
        productDialog.setLayout(new BorderLayout());
    
        JPanel editPanel = new JPanel(new GridLayout(5, 2, 10, 10));
    
        // Input Fields
        JTextField nameField = new JTextField(product.getName());
        JTextField typeField = new JTextField(product.getType());
        JTextField priceField = new JTextField(String.valueOf(product.getSellingPrice()));
        JTextField quantityField = new JTextField(String.valueOf(product.getAvailableQuantity()));
        JTextField imagePathField = new JTextField(product.getImagePath());
    
        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Type:"));
        editPanel.add(typeField);
        editPanel.add(new JLabel("Price:"));
        editPanel.add(priceField);
        editPanel.add(new JLabel("Quantity:"));
        editPanel.add(quantityField);
        editPanel.add(new JLabel("Image Path:"));
        editPanel.add(imagePathField);
    
        productDialog.add(editPanel, BorderLayout.CENTER);
    
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
    
        productDialog.add(buttonPanel, BorderLayout.SOUTH);
    
        // Save Button Action
        saveButton.addActionListener(e -> {
            try {
                product.setName(nameField.getText());
                product.setType(typeField.getText());
                product.setSellingPrice(Double.parseDouble(priceField.getText()));
                product.setAvailableQuantity(Integer.parseInt(quantityField.getText()));
                product.setImagePath(imagePathField.getText());
    
                populateProducts(); // Refresh product list
                productDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(productDialog, "Invalid input. Please enter valid numbers for price and quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        // Cancel Button Action
        cancelButton.addActionListener(e -> productDialog.dispose());
    
        productDialog.setLocationRelativeTo(frame);
        productDialog.setVisible(true);
    }
    
    private void handleLogout() {
        frame.dispose(); // Close the login window
        new LoginGUI(loginManager, inventory);
    }
}