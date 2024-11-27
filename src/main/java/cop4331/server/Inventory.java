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
import java.util.List;

public class Inventory {
    private List<Product> products;

    // Constructor
    public Inventory() {
        this.products = new ArrayList<>();
    }

    // Add a product to the inventory
    public void addProduct(Product product) {
        products.add(product);
        System.out.println(product.getName() + " added to inventory.");
    }

    // Update a product's details
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

    // Get all products in the inventory
    public List<Product> getProducts() {
        return products;
    }

    // Get a product by its ID
    public Product getProduct(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
}