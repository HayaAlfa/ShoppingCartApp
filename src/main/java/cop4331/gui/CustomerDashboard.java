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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomerDashboard {
    private JFrame frame;
    private Inventory inventory;
    private ShoppingCart cart;
    private JPanel productPanel;
    private JLabel totalLabel;

    public CustomerDashboard(User customer, Inventory inventory) {
    this.inventory = inventory;
    this.cart = new ShoppingCart(inventory); // Pass inventory to ShoppingCart
    initialize(customer);
}


    private void initialize(User customer) {
        frame = new JFrame("Customer Dashboard");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + customer.getUsername() + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(welcomeLabel, BorderLayout.NORTH);

        productPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(productPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        populateProducts();

        JPanel footerPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("TOTAL: $0.0", JLabel.LEFT);
        JButton viewCartButton = new JButton("View Cart");
        JButton checkoutButton = new JButton("Checkout");

        viewCartButton.addActionListener(e -> showCartScreen());
        checkoutButton.addActionListener(e -> checkout());

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
        checkout();
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

    private void checkout() {
        if (cart.getCartItems().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty!");
            return;
        }

        cart.getCartItems().forEach((productId, quantity) -> {
            Product product = inventory.getProduct(productId);
            if (product != null) {
                product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
            }
        });

        StringBuilder receipt = new StringBuilder("Thank you for your purchase!\n\n");
        cart.getCartItems().
    forEach((productId, quantity) -> {
            Product product = inventory.getProduct(productId);
            if (product != null) {
                receipt.append(product.getName())
                        .append(" - Quantity: ")
                        .append(quantity)
                        .append(" - Total: $")
                        .append(product.getSellingPrice() * quantity)
                        .append("\n");
            }
        });

        receipt.append("\nTotal Cost: $").append(cart.getTotalCost());
        JOptionPane.showMessageDialog(frame, receipt.toString());

        cart.clearCart();
        updateTotal();
        populateProducts();
    }

    private void updateTotal() {
        totalLabel.setText("TOTAL: $" + cart.getTotalCost());
    }

    private void updateCartTotal(JPanel cartPanel) {
        totalLabel.setText("TOTAL: $" + cart.getTotalCost());
        cartPanel.revalidate();
        cartPanel.repaint();
    }
}
