package com.example.psycounselplatform.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private boolean registered;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, boolean registered) {
        this.displayName = displayName;
        this.registered = registered;
    }

    String getDisplayName() {
        return displayName;
    }

    public boolean isRegistered() {
        return registered;
    }
}