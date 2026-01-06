package com.example.dineeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dineeasy.adapters.ReservationAdapter;
import com.example.dineeasy.database.entities.Reservation;
import com.example.dineeasy.repository.ReservationRepository;
import com.example.dineeasy.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ReservationsActivity extends AppCompatActivity implements ReservationAdapter.OnReservationClickListener {

    private BottomNavigationView bottomNavigation;
    private RecyclerView recyclerViewReservations;
    private Button btnAddReservation;
    private ReservationAdapter reservationAdapter;
    private ReservationRepository reservationRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        sessionManager = new SessionManager(this);
        reservationRepository = new ReservationRepository(this);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        recyclerViewReservations = findViewById(R.id.recyclerViewReservations);
        btnAddReservation = findViewById(R.id.btnAddReservation);

        recyclerViewReservations.setLayoutManager(new LinearLayoutManager(this));
        reservationAdapter = new ReservationAdapter(sessionManager.isStaff(), this);
        recyclerViewReservations.setAdapter(reservationAdapter);

        loadReservations();

        bottomNavigation.setSelectedItemId(R.id.navigation_reservations);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_menu) {
                startActivity(new Intent(ReservationsActivity.this, MenuActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_reservations) {
                return true;
            } else if (itemId == R.id.navigation_account) {
                startActivity(new Intent(ReservationsActivity.this, AccountActivity.class));
                finish();
                return true;
            }

            return false;
        });

        btnAddReservation.setOnClickListener(v -> {
            Intent intent = new Intent(ReservationsActivity.this, AddReservationActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReservations();
    }

    private void loadReservations() {
        if (sessionManager.isStaff()) {
            reservationRepository.getAllReservations(new ReservationRepository.DataCallback<List<Reservation>>() {
                @Override
                public void onSuccess(List<Reservation> result) {
                    runOnUiThread(() -> {
                        reservationAdapter.setReservations(result);
                        if (result.isEmpty()) {
                            Toast.makeText(ReservationsActivity.this, "No reservations found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> {
                        Toast.makeText(ReservationsActivity.this, "Error loading reservations: " + errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } else {
            reservationRepository.getReservationsByUsername(sessionManager.getUsername(), new ReservationRepository.DataCallback<List<Reservation>>() {
                @Override
                public void onSuccess(List<Reservation> result) {
                    runOnUiThread(() -> {
                        reservationAdapter.setReservations(result);
                        if (result.isEmpty()) {
                            Toast.makeText(ReservationsActivity.this, "No reservations found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> {
                        Toast.makeText(ReservationsActivity.this, "Error loading reservations: " + errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    @Override
    public void onEditClick(Reservation reservation) {
        Intent intent = new Intent(ReservationsActivity.this, AddReservationActivity.class);
        intent.putExtra("isEdit", true);
        intent.putExtra("reservationId", reservation.getId());
        intent.putExtra("date", reservation.getDate());
        intent.putExtra("time", reservation.getTime());
        intent.putExtra("people", String.valueOf(reservation.getNumberOfPeople()));
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Reservation reservation) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Reservation")
                .setMessage("Are you sure you want to delete this reservation for " + reservation.getDate() + " at " + reservation.getTime() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    reservationRepository.deleteReservation(reservation, new ReservationRepository.DataCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean result) {
                            runOnUiThread(() -> {
                                Toast.makeText(ReservationsActivity.this, "Reservation deleted successfully", Toast.LENGTH_SHORT).show();
                                loadReservations();
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            runOnUiThread(() -> {
                                Toast.makeText(ReservationsActivity.this, "Error deleting reservation: " + errorMessage, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reservationRepository.shutdown();
    }
}
