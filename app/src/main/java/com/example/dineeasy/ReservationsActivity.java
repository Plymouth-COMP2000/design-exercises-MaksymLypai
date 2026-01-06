package com.example.dineeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ReservationsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private Button btnAddReservation;
    private Button btnEditReservation1;
    private Button btnEditReservation2;
    private Button btnDeleteReservation1;
    private Button btnDeleteReservation2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        // Initialize views
        bottomNavigation = findViewById(R.id.bottom_navigation);
        btnAddReservation = findViewById(R.id.btnAddReservation);
        btnEditReservation1 = findViewById(R.id.btnEditReservation1);
        btnEditReservation2 = findViewById(R.id.btnEditReservation2);
        btnDeleteReservation1 = findViewById(R.id.btnDeleteReservation1);
        btnDeleteReservation2 = findViewById(R.id.btnDeleteReservation2);

        // Set Reservations as selected
        bottomNavigation.setSelectedItemId(R.id.navigation_reservations);

        // Handle bottom navigation clicks
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_menu) {
                startActivity(new Intent(ReservationsActivity.this, MenuActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_reservations) {
                // Already on Reservations
                return true;
            } else if (itemId == R.id.navigation_account) {
                startActivity(new Intent(ReservationsActivity.this, AccountActivity.class));
                finish();
                return true;
            }

            return false;
        });

        // Handle add reservation button click
        btnAddReservation.setOnClickListener(v -> {
            Intent intent = new Intent(ReservationsActivity.this, AddReservationActivity.class);
            startActivity(intent);
        });

        // Handle edit reservation 1 button click
        btnEditReservation1.setOnClickListener(v -> {
            Intent intent = new Intent(ReservationsActivity.this, AddReservationActivity.class);
            intent.putExtra("isEdit", true);
            intent.putExtra("bookingId", "1234");
            intent.putExtra("date", "12 Nov");
            intent.putExtra("time", "19:00");
            intent.putExtra("people", "2");
            startActivity(intent);
        });

        // Handle edit reservation 2 button click
        btnEditReservation2.setOnClickListener(v -> {
            Intent intent = new Intent(ReservationsActivity.this, AddReservationActivity.class);
            intent.putExtra("isEdit", true);
            intent.putExtra("bookingId", "1235");
            intent.putExtra("date", "14 Nov");
            intent.putExtra("time", "19:00");
            intent.putExtra("people", "2");
            startActivity(intent);
        });

        // Handle delete reservation 1 button click
        btnDeleteReservation1.setOnClickListener(v -> {
            new AlertDialog.Builder(ReservationsActivity.this)
                .setTitle("Delete Reservation")
                .setMessage("Are you sure you want to delete Booking #1234?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete reservation (API call to be implemented later)
                    Toast.makeText(ReservationsActivity.this, "Reservation deleted successfully", Toast.LENGTH_SHORT).show();
                    // In a real app, refresh the list here
                })
                .setNegativeButton("Cancel", null)
                .show();
        });

        // Handle delete reservation 2 button click
        btnDeleteReservation2.setOnClickListener(v -> {
            new AlertDialog.Builder(ReservationsActivity.this)
                .setTitle("Delete Reservation")
                .setMessage("Are you sure you want to delete Booking #1235?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete reservation (API call to be implemented later)
                    Toast.makeText(ReservationsActivity.this, "Reservation deleted successfully", Toast.LENGTH_SHORT).show();
                    // In a real app, refresh the list here
                })
                .setNegativeButton("Cancel", null)
                .show();
        });
    }
}
