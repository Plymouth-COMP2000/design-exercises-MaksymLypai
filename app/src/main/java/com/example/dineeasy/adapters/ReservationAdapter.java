package com.example.dineeasy.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dineeasy.R;
import com.example.dineeasy.database.entities.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<Reservation> reservations;
    private boolean isStaff;
    private OnReservationClickListener listener;

    public interface OnReservationClickListener {
        void onEditClick(Reservation reservation);
        void onDeleteClick(Reservation reservation);
        void onStatusChangeClick(Reservation reservation);
    }

    public ReservationAdapter(boolean isStaff, OnReservationClickListener listener) {
        this.reservations = new ArrayList<>();
        this.isStaff = isStaff;
        this.listener = listener;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.tvReservationDate.setText(reservation.getDate());
        holder.tvReservationTime.setText(reservation.getTime());
        holder.tvReservationPeople.setText(reservation.getNumberOfPeople() + " people");
        holder.tvReservationStatus.setText(reservation.getStatus());

        if (isStaff) {
            holder.tvReservationUsername.setVisibility(View.VISIBLE);
            holder.tvReservationUsername.setText("User: " + reservation.getUsername());
            holder.btnChangeStatus.setVisibility(View.VISIBLE);
        } else {
            holder.tvReservationUsername.setVisibility(View.GONE);
            holder.btnChangeStatus.setVisibility(View.GONE);
        }

        switch (reservation.getStatus()) {
            case "Confirmed":
                holder.tvReservationStatus.setTextColor(Color.parseColor("#4CAF50"));
                break;
            case "Pending":
                holder.tvReservationStatus.setTextColor(Color.parseColor("#FF9800"));
                break;
            case "Cancelled":
                holder.tvReservationStatus.setTextColor(Color.parseColor("#B22222"));
                break;
            default:
                holder.tvReservationStatus.setTextColor(Color.parseColor("#666666"));
                break;
        }

        holder.btnEditReservation.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(reservation);
            }
        });

        holder.btnChangeStatus.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStatusChangeClick(reservation);
            }
        });

        holder.btnDeleteReservation.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(reservation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvReservationDate, tvReservationTime, tvReservationPeople;
        TextView tvReservationStatus, tvReservationUsername;
        LinearLayout layoutActions;
        Button btnEditReservation, btnChangeStatus, btnDeleteReservation;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReservationDate = itemView.findViewById(R.id.tvReservationDate);
            tvReservationTime = itemView.findViewById(R.id.tvReservationTime);
            tvReservationPeople = itemView.findViewById(R.id.tvReservationPeople);
            tvReservationStatus = itemView.findViewById(R.id.tvReservationStatus);
            tvReservationUsername = itemView.findViewById(R.id.tvReservationUsername);
            layoutActions = itemView.findViewById(R.id.layoutActions);
            btnEditReservation = itemView.findViewById(R.id.btnEditReservation);
            btnChangeStatus = itemView.findViewById(R.id.btnChangeStatus);
            btnDeleteReservation = itemView.findViewById(R.id.btnDeleteReservation);
        }
    }
}
