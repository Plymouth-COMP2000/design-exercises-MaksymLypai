package com.example.dineeasy.models;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("message")
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
