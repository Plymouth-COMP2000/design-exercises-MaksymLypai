package com.example.dineeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dineeasy.database.entities.Reservation;
import com.example.dineeasy.repository.ReservationRepository;
import com.example.dineeasy.utils.NotificationHelper;
import com.example.dineeasy.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddReservationActivity extends AppCompatActivity {

    private EditText editDate;
    private EditText editTime;
    private EditText editPeople;
    private Button buttonSave;
    private Button buttonCancel;
    private BottomNavigationView bottomNavigation;
    private boolean isEditMode = false;
    private int reservationId;
    private ReservationRepository reservationRepository;
    private SessionManager sessionManager;
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        sessionManager = new SessionManager(this);
        reservationRepository = new ReservationRepository(this);
        notificationHelper = new NotificationHelper(this);

        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        editPeople = findViewById(R.id.editPeople);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("isEdit", false)) {
            isEditMode = true;
            reservationId = intent.getIntExtra("reservationId", 0);

            editDate.setText(intent.getStringExtra("date"));
            editTime.setText(intent.getStringExtra("time"));
            editPeople.setText(intent.getStringExtra("people"));

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

        buttonSave.setOnClickListener(v -> {
            String date = editDate.getText().toString().trim();
            String time = editTime.getText().toString().trim();
            String peopleStr = editPeople.getText().toString().trim();

            if (date.isEmpty() || time.isEmpty() || peopleStr.isEmpty()) {
                Toast.makeText(AddReservationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int numberOfPeople;
            try {
                numberOfPeople = Integer.parseInt(peopleStr);
            } catch (NumberFormatException e) {
                Toast.makeText(AddReservationActivity.this, "Invalid number of people", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEditMode) {
                Reservation reservation = new Reservation();
                reservation.setId(reservationId);
                reservation.setUsername(sessionManager.getUsername());
                reservation.setDate(date);
                reservation.setTime(time);
                reservation.setNumberOfPeople(numberOfPeople);
                reservation.setStatus("Confirmed");

                reservationRepository.updateReservation(reservation, new ReservationRepository.DataCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        runOnUiThread(() -> {
                            Toast.makeText(AddReservationActivity.this, "Reservation updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        runOnUiThread(() -> {
                            Toast.makeText(AddReservationActivity.this, "Error updating reservation: " + errorMessage, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            } else {
                Reservation reservation = new Reservation();
                reservation.setUsername(sessionManager.getUsername());
                reservation.setDate(date);
                reservation.setTime(time);
                reservation.setNumberOfPeople(numberOfPeople);
                reservation.setStatus("Pending");

                reservationRepository.insertReservation(reservation, new ReservationRepository.DataCallback<Long>() {
                    @Override
                    public void onSuccess(Long result) {
                        runOnUiThread(() -> {
                            // Send confirmation notification
                            notificationHelper.sendReservationConfirmation(date, time, numberOfPeople);

                            Intent confirmIntent = new Intent(AddReservationActivity.this, ConfirmationActivity.class);
                            confirmIntent.putExtra("date", date);
                            confirmIntent.putExtra("time", time);
                            confirmIntent.putExtra("people", peopleStr);
                            startActivity(confirmIntent);
                            finish();
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        runOnUiThread(() -> {
                            Toast.makeText(AddReservationActivity.this, "Error creating reservation: " + errorMessage, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        });

        buttonCancel.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reservationRepository.shutdown();
    }
}
