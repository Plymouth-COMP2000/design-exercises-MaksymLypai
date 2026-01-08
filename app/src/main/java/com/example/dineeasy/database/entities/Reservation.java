package com.example.dineeasy.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "reservations")
public class Reservation {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;  // Username of who made the reservation
    private String date;
    private String time;
    private int numberOfPeople;
    private String status;  // e.g., "Confirmed", "Pending", "Cancelled"

    public Reservation() {
    }

    @Ignore
    public Reservation(String username, String date, String time, int numberOfPeople, String status) {
        this.username = username;
        this.date = date;
        this.time = time;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
