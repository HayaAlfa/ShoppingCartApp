/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cop4331.server;

/**
 *
 * @author hayaalfakieh
 */
import java.util.ArrayList;

public class Inventory {
    private ArrayList<Product> products;

    // Constructor
    public Inventory() {
        this.products = new ArrayList<>();
    }

    // Add Product
    public void addProduct(Product product) {
        products.add(product);
        System.out.println(product.getName() + " added to inventory.");
    }

    // Update Product
    public void updateProduct(String productId, double newPrice, int newQuantity) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                product.setSellingPrice(newPrice);
                product.setAvailableQuantity(newQuantity);
                System.out.println("Product updated: " + product);
                return;
            }
        }
        System.out.println("Product not found in inventory.");
    }

    // Display Inventory
    public void displayInventory() {
        System.out.println("Inventory:");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    // Get Product by ID
    public Product getProduct(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        System.out.println("Product not found.");
        return null;
    }
}