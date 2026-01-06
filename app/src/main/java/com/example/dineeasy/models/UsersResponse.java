package com.example.dineeasy.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UsersResponse {
    @SerializedName("users")
    private List<User> users;

    public UsersResponse(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
