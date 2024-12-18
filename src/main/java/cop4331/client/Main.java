/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cop4331.client;

/**
 *
 * @author hayaalfakieh & leandro alfonso
 */

import cop4331.gui.LoginGUI;
import cop4331.server.Inventory;
import cop4331.server.LoginManager;
import cop4331.server.Product;

public class Main {
    public static void main(String[] args) {
        
        Inventory inventory = new Inventory();
        inventory.addProduct(new Product("P001", "Laptop", "Electronics", 500.0, 800.0, 10, "Images/Laptop.jpg", "seller1"));
        inventory.addProduct(new Product("P002", "Phone", "Electronics", 200.0, 400.0, 20, "Images/Phone.png", "seller1"));

        // Initialize login manager with default users
        LoginManager loginManager = new LoginManager();
        
        // Start the Login GUI
        new LoginGUI(loginManager, inventory);
    }
    
}


