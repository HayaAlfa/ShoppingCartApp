/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cop4331.server;

/**
 *
 * @author hayaalfakieh
 */
import java.util.HashMap;

public class LoginManager {
    private HashMap<String, User> users; // Maps username to User object

    // Constructor
    public LoginManager() {
        users = new HashMap<>();
        // Add some default users for testing
        users.put("customer1", new User("customer1", "password123", false));
        users.put("seller1", new User("seller1", "password123", true));
    }

    // Method to register a new user (unused for now -> will be used later)
    public boolean register(String username, String password, boolean isSeller) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return false;
        } else {
            users.put(username, new User(username, password, isSeller));
            System.out.println("User registered successfully: " + username);
            return true;
        }
    }

    // Method to validate login
    public User login(String username, String password) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            if (user.getPassword().equals(password)) {
                System.out.println("Login successful! Welcome, " + username);
                return user;
            } else {
                System.out.println("Invalid password.");
            }
        } else {
            System.out.println("User not found.");
        }
        return null;
    }

    public void registerUser(String username, String password, boolean isSeller) {
        if (users.containsKey(username)) {
            System.out.println("Error: Username already exists.");
        } else {
            users.put(username, new User(username, password, isSeller));
            System.out.println("User registered successfully: " + username);
        }
    }
}