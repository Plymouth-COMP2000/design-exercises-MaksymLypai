package com.example.dineeasy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dineeasy.R;
import com.example.dineeasy.database.entities.NotificationEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationEntity> notifications;

    public NotificationAdapter() {
        this.notifications = new ArrayList<>();
    }

    public void setNotifications(List<NotificationEntity> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationEntity notification = notifications.get(position);
        holder.tvNotificationTitle.setText(notification.getTitle());
        holder.tvNotificationMessage.setText(notification.getMessage());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
        String timeAgo = sdf.format(new Date(notification.getTimestamp()));
        holder.tvNotificationTime.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotificationTitle, tvNotificationMessage, tvNotificationTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvNotificationTime = itemView.findViewById(R.id.tvNotificationTime);
        }
    }
}
