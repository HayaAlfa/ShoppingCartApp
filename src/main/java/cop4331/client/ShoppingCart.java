/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cop4331.client;
import java.util.HashMap;
import cop4331.server.Product;
/**
 *
 * @author hayaalfakieh
 */

public class ShoppingCart {
    private HashMap<String, Integer> cartItems; // Product ID -> Quantity
    private double totalCost;

    // Constructor
    public ShoppingCart() {
        this.cartItems = new HashMap<>();
        this.totalCost = 0.0;
    }

    // Add Product to Cart
    public void addProduct(Product product, int quantity) {
        if (quantity > product.getAvailableQuantity()) {
            System.out.println("Error: Not enough stock available for " + product.getName());
            return;
        }

        cartItems.put(product.getProductId(), cartItems.getOrDefault(product.getProductId(), 0) + quantity);
        totalCost += product.getSellingPrice() * quantity;
        product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
        System.out.println(quantity + " x " + product.getName() + " added to cart.");
    }

    // Remove Product from Cart
    public void removeProduct(Product product) {
        if (cartItems.containsKey(product.getProductId())) {
            int quantity = cartItems.remove(product.getProductId());
            totalCost -= product.getSellingPrice() * quantity;
            product.setAvailableQuantity(product.getAvailableQuantity() + quantity);
            System.out.println(product.getName() + " removed from cart.");
        } else {
            System.out.println("Product not found in cart.");
        }
    }

    // Display Cart Items
    public void displayCart() {
        System.out.println("Shopping Cart:");
        for (String productId : cartItems.keySet()) {
            System.out.println("Product ID: " + productId + ", Quantity: " + cartItems.get(productId));
        }
        System.out.println("Total Cost: $" + totalCost);
    }

    // Get Total Cost
    public double getTotalCost() {
        return totalCost;
    }
}