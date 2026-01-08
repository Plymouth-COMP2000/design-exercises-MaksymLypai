package com.example.dineeasy.api;

import com.example.dineeasy.models.ApiResponse;
import com.example.dineeasy.models.User;
import com.example.dineeasy.models.UserResponse;
import com.example.dineeasy.models.UsersResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DineEasyApi {

    // Create student database
    @POST("create_student/{student_id}")
    Call<ApiResponse> createStudentDatabase(@Path("student_id") String studentId);

    // Create user
    @POST("create_user/{student_id}")
    Call<ApiResponse> createUser(@Path("student_id") String studentId, @Body User user);

    // Read all users
    @GET("read_all_users/{student_id}")
    Call<UsersResponse> getAllUsers(@Path("student_id") String studentId);

    // Read specific user
    @GET("read_user/{student_id}/{username}")
    Call<UserResponse> getUser(@Path("student_id") String studentId, @Path("username") String username);

    // Update user
    @PUT("update_user/{student_id}/{username}")
    Call<ApiResponse> updateUser(@Path("student_id") String studentId, @Path("username") String username, @Body User user);

    // Delete user
    @DELETE("delete_user/{student_id}/{username}")
    Call<ApiResponse> deleteUser(@Path("student_id") String studentId, @Path("username") String username);
}
