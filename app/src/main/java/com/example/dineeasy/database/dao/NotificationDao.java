package com.example.dineeasy.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dineeasy.database.entities.NotificationEntity;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    long insertNotification(NotificationEntity notification);

    @Query("SELECT * FROM notifications WHERE username = :username ORDER BY timestamp DESC LIMIT 10")
    List<NotificationEntity> getRecentNotifications(String username);

    @Query("DELETE FROM notifications WHERE id = :id")
    void deleteNotification(int id);

    @Query("DELETE FROM notifications WHERE username = :username")
    void deleteAllNotifications(String username);
}
