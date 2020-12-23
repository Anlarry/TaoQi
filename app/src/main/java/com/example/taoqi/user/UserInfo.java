package com.example.taoqi.user;

public class UserInfo {
    public int userId;
    public boolean Customer;
    public String username = null;
    public String email = null;
    public UserInfo() {}
    public UserInfo (int userId, String username, String email, boolean Customer) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.Customer = Customer;
    }
}
