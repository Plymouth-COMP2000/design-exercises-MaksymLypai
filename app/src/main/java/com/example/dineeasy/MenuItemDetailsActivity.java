package com.example.dineeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dineeasy.database.entities.MenuItem;
import com.example.dineeasy.repository.MenuRepository;
import com.example.dineeasy.utils.NotificationHelper;
import com.example.dineeasy.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuItemDetailsActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private EditText editName, editDescription, editPrice, editCategory, editImageUrl;
    private Button buttonSave, buttonCancel;
    private BottomNavigationView bottomNavigation;

    private MenuRepository menuRepository;
    private NotificationHelper notificationHelper;
    private SessionManager sessionManager;

    private MenuItem currentMenuItem;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_details);

        sessionManager = new SessionManager(this);
        menuRepository = new MenuRepository(this);
        notificationHelper = new NotificationHelper(this);

        initializeViews();
        setupBottomNavigation();
        loadMenuItem();
        setupButtonListeners();
    }

    private void initializeViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);
        editCategory = findViewById(R.id.editCategory);
        editImageUrl = findViewById(R.id.editImageUrl);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.navigation_menu);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_menu) {
                startActivity(new Intent(MenuItemDetailsActivity.this, MenuActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_reservations) {
                startActivity(new Intent(MenuItemDetailsActivity.this, ReservationsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_account) {
                startActivity(new Intent(MenuItemDetailsActivity.this, AccountActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }

    private void loadMenuItem() {
        Intent intent = getIntent();
        if (intent.hasExtra("MENU_ITEM_ID")) {
            isEditMode = true;
            int menuItemId = intent.getIntExtra("MENU_ITEM_ID", -1);

            textViewTitle.setText("Edit Menu Item");

            menuRepository.getMenuItemById(menuItemId, new MenuRepository.DataCallback<MenuItem>() {
                @Override
                public void onSuccess(MenuItem result) {
                    runOnUiThread(() -> {
                        currentMenuItem = result;
                        populateFields();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> {
                        Toast.makeText(MenuItemDetailsActivity.this, "Error loading menu item: " + errorMessage, Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            });
        } else {
            isEditMode = false;
            textViewTitle.setText("Add Menu Item");
        }
    }

    private void populateFields() {
        if (currentMenuItem != null) {
            editName.setText(currentMenuItem.getName());
            editDescription.setText(currentMenuItem.getDescription());
            editPrice.setText(String.valueOf(currentMenuItem.getPrice()));
            editCategory.setText(currentMenuItem.getCategory());
            editImageUrl.setText(currentMenuItem.getImageUrl());
        }
    }

    private void setupButtonListeners() {
        buttonSave.setOnClickListener(v -> saveMenuItem());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void saveMenuItem() {
        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String priceStr = editPrice.getText().toString().trim();
        String category = editCategory.getText().toString().trim();
        String imageUrl = editImageUrl.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            editName.setError("Name is required");
            editName.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            editDescription.setError("Description is required");
            editDescription.requestFocus();
            return;
        }

        if (priceStr.isEmpty()) {
            editPrice.setError("Price is required");
            editPrice.requestFocus();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                editPrice.setError("Price must be greater than 0");
                editPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editPrice.setError("Invalid price format");
            editPrice.requestFocus();
            return;
        }

        if (category.isEmpty()) {
            editCategory.setError("Category is required");
            editCategory.requestFocus();
            return;
        }

        if (isEditMode) {
            // Update existing menu item
            currentMenuItem.setName(name);
            currentMenuItem.setDescription(description);
            currentMenuItem.setPrice(price);
            currentMenuItem.setCategory(category);
            currentMenuItem.setImageUrl(imageUrl);

            menuRepository.updateMenuItem(currentMenuItem, new MenuRepository.DataCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    runOnUiThread(() -> {
                        Toast.makeText(MenuItemDetailsActivity.this, "Menu item updated successfully", Toast.LENGTH_SHORT).show();
                        notificationHelper.sendMenuUpdate(name, "updated");
                        finish();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> {
                        Toast.makeText(MenuItemDetailsActivity.this, "Error updating menu item: " + errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } else {
            // Create new menu item
            MenuItem newMenuItem = new MenuItem(name, description, price, imageUrl, category);

            menuRepository.insertMenuItem(newMenuItem, new MenuRepository.DataCallback<Long>() {
                @Override
                public void onSuccess(Long result) {
                    runOnUiThread(() -> {
                        Toast.makeText(MenuItemDetailsActivity.this, "Menu item added successfully", Toast.LENGTH_SHORT).show();
                        notificationHelper.sendMenuUpdate(name, "added");
                        finish();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> {
                        Toast.makeText(MenuItemDetailsActivity.this, "Error adding menu item: " + errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        menuRepository.shutdown();
    }
}
