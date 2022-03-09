package com.example.psycounselplatform.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private boolean registered;

    public LoggedInUser(String userId, String displayName, boolean registered) {
        this.userId = userId;
        this.displayName = displayName;
        this.registered = registered;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isRegistered() {
        return registered;
    }
}