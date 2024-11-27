/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cop4331.server;
/**
 *
 * @author hayaalfakieh
 */
public class Product {
    private String productId;
    private String name;
    private String type;
    private double invoicePrice;
    private double sellingPrice;
    private int availableQuantity;

    // Constructor
    public Product(String productId, String name, String type, double invoicePrice, double sellingPrice, int availableQuantity) {
        this.productId = productId;
        this.name = name;
        this.type = type;
        this.invoicePrice = invoicePrice;
        this.sellingPrice = sellingPrice;
        this.availableQuantity = availableQuantity;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getInvoicePrice() {
        return invoicePrice;
    }

    public void setInvoicePrice(double invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public String toString() {
        return "Product ID: " + productId + ", Name: " + name + ", Type: " + type + 
               ", Price: $" + sellingPrice + ", Available: " + availableQuantity;
    }
}