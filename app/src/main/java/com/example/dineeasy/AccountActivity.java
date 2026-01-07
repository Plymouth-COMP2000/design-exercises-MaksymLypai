package com.example.dineeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.dineeasy.models.User;
import com.example.dineeasy.repository.UserRepository;
import com.example.dineeasy.utils.Constants;
import com.example.dineeasy.utils.SessionManager;

public class AccountActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private TextView userName;
    private TextView userEmail;
    private Button btnLogout;
    private SwitchCompat switchReservationUpdates;
    private SwitchCompat switchMenuUpdates;
    private SessionManager sessionManager;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Initialize session manager and repository
        sessionManager = new SessionManager(this);
        userRepository = new UserRepository();

        // Initialize views
        bottomNavigation = findViewById(R.id.bottom_navigation);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        btnLogout = findViewById(R.id.btnLogout);
        switchReservationUpdates = findViewById(R.id.switchReservationUpdates);
        switchMenuUpdates = findViewById(R.id.switchMenuUpdates);

        // Set Account as selected
        bottomNavigation.setSelectedItemId(R.id.navigation_account);

        // Load user data
        loadUserData();

        // Load notification preferences
        switchReservationUpdates.setChecked(sessionManager.areReservationNotificationsEnabled());
        switchMenuUpdates.setChecked(sessionManager.areMenuNotificationsEnabled());

        // Handle notification switches
        switchReservationUpdates.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sessionManager.setReservationNotificationsEnabled(isChecked);
        });

        switchMenuUpdates.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sessionManager.setMenuNotificationsEnabled(isChecked);
        });

        // Handle bottom navigation clicks
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_menu) {
                startActivity(new Intent(AccountActivity.this, MenuActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_reservations) {
                startActivity(new Intent(AccountActivity.this, ReservationsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_account) {
                // Already on Account
                return true;
            }

            return false;
        });

        // Handle logout button
        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(AccountActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserData() {
        String username = sessionManager.getUsername();

        if (username != null) {
            // Fetch user details from API
            userRepository.getAllUsers(new UserRepository.ApiCallback<java.util.List<User>>() {
                @Override
                public void onSuccess(java.util.List<User> users) {
                    runOnUiThread(() -> {
                        // Find current user
                        for (User user : users) {
                            if (user.getUsername().equals(username)) {
                                // Display user information
                                userName.setText(user.getFirstname() + " " + user.getLastname());
                                userEmail.setText(user.getEmail());
                                break;
                            }
                        }
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> {
                        // Fallback to username if API fails
                        userName.setText(username);
                        userEmail.setText("Unable to load email");
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userRepository.shutdown();
    }
}
