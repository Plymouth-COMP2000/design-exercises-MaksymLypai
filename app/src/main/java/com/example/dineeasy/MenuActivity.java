package com.example.dineeasy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dineeasy.adapters.MenuAdapter;
import com.example.dineeasy.database.entities.MenuItem;
import com.example.dineeasy.repository.MenuRepository;
import com.example.dineeasy.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MenuActivity extends AppCompatActivity implements MenuAdapter.OnMenuItemClickListener {

    private BottomNavigationView bottomNavigation;
    private RecyclerView recyclerViewMenu;
    private SearchView searchView;
    private FloatingActionButton fabAddItem;
    private MenuAdapter menuAdapter;
    private MenuRepository menuRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sessionManager = new SessionManager(this);
        menuRepository = new MenuRepository(this);

        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        searchView = findViewById(R.id.searchView);
        fabAddItem = findViewById(R.id.fabAddItem);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter(sessionManager.isStaff(), this);
        recyclerViewMenu.setAdapter(menuAdapter);

        if (sessionManager.isStaff()) {
            fabAddItem.setVisibility(View.VISIBLE);
            fabAddItem.setOnClickListener(v -> {
                Toast.makeText(this, "Add menu item feature coming soon", Toast.LENGTH_SHORT).show();
            });
        }

        loadMenuItems();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                menuAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                menuAdapter.filter(newText);
                return true;
            }
        });

        bottomNavigation.setSelectedItemId(R.id.navigation_menu);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_menu) {
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

    private void loadMenuItems() {
        menuRepository.getAllMenuItems(new MenuRepository.DataCallback<List<MenuItem>>() {
            @Override
            public void onSuccess(List<MenuItem> result) {
                runOnUiThread(() -> {
                    menuAdapter.setMenuItems(result);
                    if (result.isEmpty()) {
                        Toast.makeText(MenuActivity.this, "No menu items found", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(MenuActivity.this, "Error loading menu: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public void onEditClick(MenuItem menuItem) {
        Toast.makeText(this, "Edit " + menuItem.getName() + " - Coming soon", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(MenuItem menuItem) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Menu Item")
                .setMessage("Are you sure you want to delete " + menuItem.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    menuRepository.deleteMenuItem(menuItem, new MenuRepository.DataCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean result) {
                            runOnUiThread(() -> {
                                Toast.makeText(MenuActivity.this, menuItem.getName() + " deleted successfully", Toast.LENGTH_SHORT).show();
                                loadMenuItems();
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            runOnUiThread(() -> {
                                Toast.makeText(MenuActivity.this, "Error deleting item: " + errorMessage, Toast.LENGTH_SHORT).show();
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
        menuRepository.shutdown();
    }
}
