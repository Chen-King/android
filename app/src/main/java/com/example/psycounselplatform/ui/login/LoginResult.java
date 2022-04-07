package com.example.psycounselplatform.ui.login;

import androidx.annotation.Nullable;

import com.example.psycounselplatform.data.model.LoggedInUser;
import com.example.psycounselplatform.util.MyApplication;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUser success;
    @Nullable
    private String error;

    LoginResult(@Nullable String error) {
        this.error = error;
    }

    LoginResult(@Nullable Integer errorCode) {
        this.error = MyApplication.getContext().getString(errorCode);
    }

    LoginResult(@Nullable LoggedInUser success) {
        this.success = success;
    }

    @Nullable
    LoggedInUser getSuccess() {
        return success;
    }

    @Nullable
    String getError() {
        return error;
    }
}