package com.example.dineeasy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConfirmationActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Initialize bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Set Reservations as selected
        bottomNavigation.setSelectedItemId(R.id.navigation_reservations);

        // Handle bottom navigation clicks
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_menu) {
                startActivity(new Intent(ConfirmationActivity.this, MenuActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_reservations) {
                startActivity(new Intent(ConfirmationActivity.this, ReservationsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_account) {
                startActivity(new Intent(ConfirmationActivity.this, AccountActivity.class));
                finish();
                return true;
            }

            return false;
        });

        // Get reservation details from intent
        Intent intent = getIntent();
        if (intent != null) {
            String date = intent.getStringExtra("date");
            String time = intent.getStringExtra("time");
            String people = intent.getStringExtra("people");

            // Details can be displayed in the confirmation screen if needed
            // Currently using static layout confirmation message
        }
    }
}
