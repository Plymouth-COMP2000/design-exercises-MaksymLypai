package com.example.dineeasy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dineeasy.models.User;
import com.example.dineeasy.repository.MenuRepository;
import com.example.dineeasy.repository.UserRepository;
import com.example.dineeasy.utils.Constants;
import com.example.dineeasy.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText emailInput;
    private EditText passwordInput;
    private Button btnLogin;
    private UserRepository userRepository;
    private MenuRepository menuRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize repository and session manager
        userRepository = new UserRepository();
        menuRepository = new MenuRepository(this);
        sessionManager = new SessionManager(this);

        // Check if already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToHub();
            return;
        }

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        btnLogin = findViewById(R.id.btnLogin);

        // Initialize database and create test user on first run
        initializeDatabaseAndTestUser();

        // Set login button click listener
        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void initializeDatabaseAndTestUser() {
        Toast.makeText(this, "Initializing database...", Toast.LENGTH_SHORT).show();

        // Initialize menu database with sample data
        menuRepository.initializeSampleData(new MenuRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Log.d(TAG, "Menu database initialized");
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "Menu init error: " + errorMessage);
            }
        });

        userRepository.initializeDatabase(new UserRepository.ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Database initialized");
                    createTestGuestUser();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Database init error: " + errorMessage);
                    Toast.makeText(MainActivity.this, "Database error: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void createTestGuestUser() {
        // Create test guest user
        User testGuest = new User(
                "guest_user",
                "test123",
                "Test",
                "Guest",
                "guest@test.com",
                "1234567890",
                Constants.USER_TYPE_GUEST
        );

        userRepository.createUser(testGuest, new UserRepository.ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Test guest user created successfully");
                    Toast.makeText(MainActivity.this,
                        "Test user ready!\nEmail: guest@test.com\nPassword: test123",
                        Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    // User might already exist, that's okay
                    Log.d(TAG, "Guest user creation: " + errorMessage);
                    Toast.makeText(MainActivity.this,
                        "Login with:\nEmail: guest@test.com\nPassword: test123",
                        Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button during login
        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        // Validate credentials via API
        userRepository.validateLogin(email, password, new UserRepository.ApiCallback<User>() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    // Save session
                    sessionManager.createLoginSession(user.getUsername(), user.getUsertype());

                    Toast.makeText(MainActivity.this,
                        "Welcome, " + user.getFirstname() + "!",
                        Toast.LENGTH_SHORT).show();

                    // Navigate to Hub
                    navigateToHub();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Sign In");
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void navigateToHub() {
        Intent intent = new Intent(MainActivity.this, HubActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userRepository.shutdown();
        menuRepository.shutdown();
    }
}
