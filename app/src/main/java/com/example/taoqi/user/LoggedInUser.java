package com.example.taoqi.user;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser extends UserInfo {

    public LoggedInUser(int userId,String username, String email, boolean Customer) {
        super(userId, username, email, Customer);
    }

    public String getUserName() {
        return username;
    }

    public String getDisplayName () {
        return username;
    }
}