package com.example.dineeasy.repository;

import android.content.Context;

import com.example.dineeasy.database.AppDatabase;
import com.example.dineeasy.database.dao.ReservationDao;
import com.example.dineeasy.database.entities.Reservation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReservationRepository {
    private final ReservationDao reservationDao;
    private final ExecutorService executorService;

    public ReservationRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        this.reservationDao = database.reservationDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    // Callback interface
    public interface DataCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    // Get all reservations
    public void getAllReservations(DataCallback<List<Reservation>> callback) {
        executorService.execute(() -> {
            try {
                List<Reservation> reservations = reservationDao.getAllReservations();
                callback.onSuccess(reservations);
            } catch (Exception e) {
                callback.onError("Error fetching reservations: " + e.getMessage());
            }
        });
    }

    // Get reservation by ID
    public void getReservationById(int id, DataCallback<Reservation> callback) {
        executorService.execute(() -> {
            try {
                Reservation reservation = reservationDao.getReservationById(id);
                callback.onSuccess(reservation);
            } catch (Exception e) {
                callback.onError("Error fetching reservation: " + e.getMessage());
            }
        });
    }

    // Get reservations by username
    public void getReservationsByUsername(String username, DataCallback<List<Reservation>> callback) {
        executorService.execute(() -> {
            try {
                List<Reservation> reservations = reservationDao.getReservationsByUsername(username);
                callback.onSuccess(reservations);
            } catch (Exception e) {
                callback.onError("Error fetching user reservations: " + e.getMessage());
            }
        });
    }

    // Get reservations by status
    public void getReservationsByStatus(String status, DataCallback<List<Reservation>> callback) {
        executorService.execute(() -> {
            try {
                List<Reservation> reservations = reservationDao.getReservationsByStatus(status);
                callback.onSuccess(reservations);
            } catch (Exception e) {
                callback.onError("Error fetching reservations by status: " + e.getMessage());
            }
        });
    }

    // Insert reservation
    public void insertReservation(Reservation reservation, DataCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                long id = reservationDao.insert(reservation);
                callback.onSuccess(id);
            } catch (Exception e) {
                callback.onError("Error creating reservation: " + e.getMessage());
            }
        });
    }

    // Update reservation
    public void updateReservation(Reservation reservation, DataCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                reservationDao.update(reservation);
                callback.onSuccess(true);
            } catch (Exception e) {
                callback.onError("Error updating reservation: " + e.getMessage());
            }
        });
    }

    // Delete reservation
    public void deleteReservation(Reservation reservation, DataCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                reservationDao.delete(reservation);
                callback.onSuccess(true);
            } catch (Exception e) {
                callback.onError("Error deleting reservation: " + e.getMessage());
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
