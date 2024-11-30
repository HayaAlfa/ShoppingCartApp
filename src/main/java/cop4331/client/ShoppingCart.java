package cop4331.client;

import cop4331.server.Inventory;
import cop4331.server.Product;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hayaalfakieh
 */
public class ShoppingCart {
    private HashMap<String, Integer> cartItems; // Product ID -> Quantity
    private double totalCost;
    private Inventory inventory; // Reference to inventory


    // Constructor
    public ShoppingCart(Inventory inventory) {
        this.cartItems = new HashMap<>();
        this.totalCost = 0.0;
        this.inventory = inventory; // Assign inventory instance

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

    // Get Cart Items (New Method)
    public Map<String, Integer> getCartItems() {
        return cartItems; // Returns the map of Product IDs and their quantities
    }

    // Clear Cart (New Method)
    public void clearCart() {
        // Restore product quantities to inventory
        cartItems.forEach((productId, quantity) -> {
            // Restore the quantity to the Product object (Assumes Product objects are managed externally)
            System.out.println("Clearing item: " + productId + ", Quantity: " + quantity);
        });
        cartItems.clear();
        totalCost = 0.0;
    }

    public void updateProductQuantity(String productId, int quantity) {
    if (cartItems.containsKey(productId)) {
        cartItems.put(productId, quantity); // Update the quantity in the cart
        recalculateTotal(); // Update the total cost
    }
}

    private void recalculateTotal() {
    totalCost = cartItems.entrySet().stream()
            .mapToDouble(entry -> {
                String productId = entry.getKey();
                int quantity = entry.getValue();
                Product product = inventory.getProduct(productId);
                return product != null ? product.getSellingPrice() * quantity : 0;
            }).sum();
    }

}
