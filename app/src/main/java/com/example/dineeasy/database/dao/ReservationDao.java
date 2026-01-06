package com.example.dineeasy.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dineeasy.database.entities.Reservation;

import java.util.List;

@Dao
public interface ReservationDao {

    @Insert
    long insert(Reservation reservation);

    @Update
    void update(Reservation reservation);

    @Delete
    void delete(Reservation reservation);

    @Query("SELECT * FROM reservations ORDER BY date DESC, time DESC")
    List<Reservation> getAllReservations();

    @Query("SELECT * FROM reservations WHERE id = :id")
    Reservation getReservationById(int id);

    @Query("SELECT * FROM reservations WHERE username = :username ORDER BY date DESC, time DESC")
    List<Reservation> getReservationsByUsername(String username);

    @Query("SELECT * FROM reservations WHERE status = :status ORDER BY date DESC, time DESC")
    List<Reservation> getReservationsByStatus(String status);

    @Query("DELETE FROM reservations")
    void deleteAll();
}
