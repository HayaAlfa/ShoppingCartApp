/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cop4331.server;

/**
 *
 * @author hayaalfakieh & leandro alfonso
 */
import java.util.ArrayList;
import java.util.List;


public class User {
    private String username;
    private String password;
    private boolean isSeller; // true if seller, false if customer
    private List<Product> products; // List of products for sellers only

    // Constructor
    public User(String username, String password, boolean isSeller) {
        this.username = username;
        this.password = password;
        this.isSeller = isSeller;
        this.products = isSeller ? new ArrayList<>() : null; // Initialize products only if seller
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean isSeller) {
        this.isSeller = isSeller;
        // Initialize or clear products when the role changes
        if (isSeller && this.products == null) {
            this.products = new ArrayList<>();
        } else if (!isSeller) {
            this.products = null;
        }
    }

    public List<Product> getProducts() {
        if (!isSeller) {
            throw new UnsupportedOperationException("Customers do not have a products list.");
        }
        return products;
    }

    public void setProducts(List<Product> products) {
        if (!isSeller) {
            throw new UnsupportedOperationException("Customers cannot have a products list.");
        }
        this.products = products;
    }

    public void addProduct(Product product) {
        if (!isSeller) {
            throw new UnsupportedOperationException("Only sellers can add products.");
        }
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        if (!isSeller) {
            throw new UnsupportedOperationException("Only sellers can remove products.");
        }
        this.products.remove(product);
    }

    @Override
    public String toString() {
        return "User: " + username + ", Role: " + (isSeller ? "Seller" : "Customer") +
               (isSeller ? ", Products: " + (products.isEmpty() ? "None" : products.toString()) : "");
    }
}
