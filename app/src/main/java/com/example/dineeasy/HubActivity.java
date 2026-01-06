package com.example.dineeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HubActivity extends AppCompatActivity {

    private Button btnMenu;
    private Button btnReservations;
    private Button btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        // Initialize buttons
        btnMenu = findViewById(R.id.btnMenu);
        btnReservations = findViewById(R.id.btnReservations);
        btnAccount = findViewById(R.id.btnAccount);

        // Set click listeners
        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(HubActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        btnReservations.setOnClickListener(v -> {
            Intent intent = new Intent(HubActivity.this, ReservationsActivity.class);
            startActivity(intent);
        });

        btnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(HubActivity.this, AccountActivity.class);
            startActivity(intent);
        });
    }
}
