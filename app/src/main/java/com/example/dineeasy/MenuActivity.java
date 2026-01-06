package com.example.dineeasy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize bottom navigation
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Set Menu as selected
        bottomNavigation.setSelectedItemId(R.id.navigation_menu);

        // Handle navigation item clicks
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_menu) {
                // Already on Menu
                return true;
            } else if (itemId == R.id.navigation_reservations) {
                startActivity(new Intent(MenuActivity.this, ReservationsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_account) {
                startActivity(new Intent(MenuActivity.this, AccountActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }
}
