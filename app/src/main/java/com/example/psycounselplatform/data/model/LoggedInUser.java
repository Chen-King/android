package com.example.psycounselplatform.data.model;

import java.io.Serializable;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Serializable {

    private Integer id;
    private String role;
    private boolean registered = true;
    private String token;

    public Integer getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public boolean isRegistered() {
        return registered;
    }

    public String getToken() {
        return token;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}