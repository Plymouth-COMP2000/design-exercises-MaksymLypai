package com.example.dineeasy.repository;

import android.util.Log;

import com.example.dineeasy.api.DineEasyApi;
import com.example.dineeasy.api.RetrofitClient;
import com.example.dineeasy.models.ApiResponse;
import com.example.dineeasy.models.User;
import com.example.dineeasy.models.UsersResponse;
import com.example.dineeasy.utils.Constants;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private final DineEasyApi api;
    private final ExecutorService executorService;

    public UserRepository() {
        this.api = RetrofitClient.getInstance().getApi();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    // Callback interface for async operations
    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    // Initialize database (create if not exists)
    public void initializeDatabase(ApiCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                Call<ApiResponse> call = api.createStudentDatabase(Constants.STUDENT_ID);
                Response<ApiResponse> response = call.execute();

                if (response.isSuccessful()) {
                    Log.d(TAG, "Database initialized successfully");
                    callback.onSuccess(true);
                } else {
                    // Database might already exist, that's okay
                    Log.d(TAG, "Database creation response: " + response.code());
                    callback.onSuccess(true);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error initializing database", e);
                callback.onError("Database initialization failed: " + e.getMessage());
            }
        });
    }

    // Create a new user
    public void createUser(User user, ApiCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                Call<ApiResponse> call = api.createUser(Constants.STUDENT_ID, user);
                Response<ApiResponse> response = call.execute();

                if (response.isSuccessful()) {
                    Log.d(TAG, "User created successfully: " + user.getUsername());
                    callback.onSuccess(true);
                } else {
                    String errorMsg = "Failed to create user: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error creating user", e);
                callback.onError("Error creating user: " + e.getMessage());
            }
        });
    }

    // Get all users
    public void getAllUsers(ApiCallback<List<User>> callback) {
        executorService.execute(() -> {
            try {
                Call<UsersResponse> call = api.getAllUsers(Constants.STUDENT_ID);
                Response<UsersResponse> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body().getUsers();
                    Log.d(TAG, "Fetched " + users.size() + " users");
                    callback.onSuccess(users);
                } else {
                    String errorMsg = "Failed to fetch users: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching users", e);
                callback.onError("Error fetching users: " + e.getMessage());
            }
        });
    }

    // Validate login credentials
    public void validateLogin(String email, String password, ApiCallback<User> callback) {
        executorService.execute(() -> {
            try {
                // Fetch all users
                Call<UsersResponse> call = api.getAllUsers(Constants.STUDENT_ID);
                Response<UsersResponse> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body().getUsers();

                    // Find user by email and password
                    User matchedUser = null;
                    for (User user : users) {
                        if (user.getEmail().equalsIgnoreCase(email) &&
                            user.getPassword().equals(password)) {
                            matchedUser = user;
                            break;
                        }
                    }

                    if (matchedUser != null) {
                        Log.d(TAG, "Login successful for: " + matchedUser.getUsername());
                        callback.onSuccess(matchedUser);
                    } else {
                        Log.d(TAG, "Invalid credentials for email: " + email);
                        callback.onError("Invalid email or password");
                    }
                } else {
                    String errorMsg = "Failed to validate login: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error validating login", e);
                callback.onError("Network error: " + e.getMessage());
            }
        });
    }

    // Shutdown executor
    public void shutdown() {
        executorService.shutdown();
    }
}
