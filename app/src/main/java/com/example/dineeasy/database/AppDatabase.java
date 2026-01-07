package com.example.dineeasy.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dineeasy.database.dao.MenuItemDao;
import com.example.dineeasy.database.dao.NotificationDao;
import com.example.dineeasy.database.dao.ReservationDao;
import com.example.dineeasy.database.entities.MenuItem;
import com.example.dineeasy.database.entities.NotificationEntity;
import com.example.dineeasy.database.entities.Reservation;

@Database(entities = {MenuItem.class, Reservation.class, NotificationEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract MenuItemDao menuItemDao();
    public abstract ReservationDao reservationDao();
    public abstract NotificationDao notificationDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "dineeasy_database"
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }
}
