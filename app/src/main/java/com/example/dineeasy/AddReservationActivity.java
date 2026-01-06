package com.example.dineeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddReservationActivity extends AppCompatActivity {

    private EditText editDate;
    private EditText editTime;
    private EditText editPeople;
    private Button buttonSave;
    private Button buttonCancel;
    private BottomNavigationView bottomNavigation;
    private boolean isEditMode = false;
    private String bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        // Initialize views
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        editPeople = findViewById(R.id.editPeople);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Check if in edit mode
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("isEdit", false)) {
            isEditMode = true;
            bookingId = intent.getStringExtra("bookingId");

            // Pre-fill form with existing data
            editDate.setText(intent.getStringExtra("date"));
            editTime.setText(intent.getStringExtra("time"));
            editPeople.setText(intent.getStringExtra("people"));

            // Change button text to "Update"
            buttonSave.setText("Update");
        }

        // Set Reservations as selected in bottom nav
        bottomNavigation.setSelectedItemId(R.id.navigation_reservations);

        // Handle bottom navigation clicks
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_menu) {
                startActivity(new Intent(AddReservationActivity.this, MenuActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_reservations) {
                startActivity(new Intent(AddReservationActivity.this, ReservationsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_account) {
                startActivity(new Intent(AddReservationActivity.this, AccountActivity.class));
                finish();
                return true;
            }

            return false;
        });

        // Handle Save/Update button
        buttonSave.setOnClickListener(v -> {
            String date = editDate.getText().toString().trim();
            String time = editTime.getText().toString().trim();
            String people = editPeople.getText().toString().trim();

            // Basic validation
            if (date.isEmpty() || time.isEmpty() || people.isEmpty()) {
                Toast.makeText(AddReservationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEditMode) {
                // Update existing reservation
                Toast.makeText(AddReservationActivity.this, "Reservation updated successfully", Toast.LENGTH_SHORT).show();
                // Go back to reservations list
                finish();
            } else {
                // Create new reservation - navigate to confirmation screen
                Intent confirmIntent = new Intent(AddReservationActivity.this, ConfirmationActivity.class);
                confirmIntent.putExtra("date", date);
                confirmIntent.putExtra("time", time);
                confirmIntent.putExtra("people", people);
                startActivity(confirmIntent);
                finish();
            }
        });

        // Handle Cancel button
        buttonCancel.setOnClickListener(v -> {
            finish(); // Go back to previous screen
        });
    }
}
