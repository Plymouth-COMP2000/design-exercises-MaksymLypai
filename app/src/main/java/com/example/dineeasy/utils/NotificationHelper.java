package com.example.dineeasy.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.dineeasy.R;
import com.example.dineeasy.ReservationsActivity;
import com.example.dineeasy.database.AppDatabase;
import com.example.dineeasy.database.entities.NotificationEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationHelper {
    private static final String CHANNEL_ID = "dineeasy_reservations";
    private static final String CHANNEL_NAME = "Reservation Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notifications for reservation confirmations and updates";
    private static final int NOTIFICATION_ID_BASE = 1000;

    private final Context context;
    private final NotificationManager notificationManager;
    private final SessionManager sessionManager;
    private final AppDatabase database;
    private final ExecutorService executorService;

    public NotificationHelper(Context context) {
        this.context = context.getApplicationContext();
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.sessionManager = new SessionManager(context);
        this.database = AppDatabase.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendReservationConfirmation(String date, String time, int numberOfPeople) {
        if (!sessionManager.areReservationNotificationsEnabled()) {
            return;
        }

        String title = "Reservation Received";
        String message = String.format("Your reservation for %d people on %s at %s has been received and is pending confirmation.",
                numberOfPeople, date, time);

        sendNotification(NOTIFICATION_ID_BASE, title, message, ReservationsActivity.class);
    }

    public void sendReservationStatusUpdate(String username, String date, String time, String newStatus) {
        if (!sessionManager.areReservationNotificationsEnabled()) {
            return;
        }

        String title = "Reservation Status Updated";
        String message = String.format("Your reservation on %s at %s is now %s.",
                date, time, newStatus);

        sendNotification(NOTIFICATION_ID_BASE + 1, title, message, ReservationsActivity.class, username);
    }

    public void sendMenuUpdate(String itemName, String action) {
        if (!sessionManager.areMenuNotificationsEnabled()) {
            return;
        }

        String title = "Menu Updated";
        String message;
        if ("added".equals(action)) {
            message = String.format("New item added to menu: %s", itemName);
        } else if ("updated".equals(action)) {
            message = String.format("Menu item updated: %s", itemName);
        } else {
            message = String.format("Menu item removed: %s", itemName);
        }

        sendNotification(NOTIFICATION_ID_BASE + 2, title, message, com.example.dineeasy.MenuActivity.class);
    }

    private void sendNotification(int notificationId, String title, String message, Class<?> activityClass) {
        sendNotification(notificationId, title, message, activityClass, sessionManager.getUsername());
    }

    private void sendNotification(int notificationId, String title, String message, Class<?> activityClass, String targetUsername) {
        // Save to database
        saveNotificationToDatabase(title, message, targetUsername);

        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(notificationId, builder.build());
    }

    private void saveNotificationToDatabase(String title, String message, String targetUsername) {
        executorService.execute(() -> {
            NotificationEntity notification = new NotificationEntity();
            notification.setUsername(targetUsername);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setTimestamp(System.currentTimeMillis());
            notification.setRead(false);

            database.notificationDao().insertNotification(notification);
        });
    }
}
