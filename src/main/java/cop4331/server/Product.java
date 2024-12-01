/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cop4331.server;
/**
 *
 * @author hayaalfakieh & leandro alfonso
 */
public class Product {
    private String productId;
    private String name;
    private String type;
    private double invoicePrice;
    private double sellingPrice;
    private int availableQuantity;
    private String imagePath; 
    private String sellerUsername;

    
    // Constructor
    public Product(String productId, String name, String type, double invoicePrice, double sellingPrice, int availableQuantity, String imagePath, String sellerUsername) {
        this.productId = productId;
        this.name = name;
        this.type = type;
        this.invoicePrice = invoicePrice;
        this.sellingPrice = sellingPrice;
        this.availableQuantity = availableQuantity;
        this.imagePath = imagePath;
        this.sellerUsername = sellerUsername;

    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }
    
    // Ideally this would be removed and add functionality that sets the ProductId automatically
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

     public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    @Override
    public String toString() {
        return "Product ID: " + productId + ", Name: " + name + ", Type: " + type + 
               ", Price: $" + sellingPrice + ", Available: " + availableQuantity;
    }
}