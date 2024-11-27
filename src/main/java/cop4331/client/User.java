/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cop4331.client;

/**
 *
 * @author hayaalfakieh
 */
public class User {
    private String username;
    private String password;
    private boolean isSeller;

    // Constructor
    public User(String username, String password, boolean isSeller) {
        this.username = username;
        this.password = password;
        this.isSeller = isSeller;
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
    }
}
