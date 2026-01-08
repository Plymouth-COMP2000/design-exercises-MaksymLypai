package com.example.dineeasy.models;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("user")
    private User user;

    public UserResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
