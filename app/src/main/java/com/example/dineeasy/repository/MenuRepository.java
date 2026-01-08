package com.example.dineeasy.repository;

import android.content.Context;

import com.example.dineeasy.database.AppDatabase;
import com.example.dineeasy.database.dao.MenuItemDao;
import com.example.dineeasy.database.entities.MenuItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuRepository {
    private final MenuItemDao menuItemDao;
    private final ExecutorService executorService;

    public MenuRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        this.menuItemDao = database.menuItemDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    // Callback interface
    public interface DataCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    // Get all menu items
    public void getAllMenuItems(DataCallback<List<MenuItem>> callback) {
        executorService.execute(() -> {
            try {
                List<MenuItem> items = menuItemDao.getAllMenuItems();
                callback.onSuccess(items);
            } catch (Exception e) {
                callback.onError("Error fetching menu items: " + e.getMessage());
            }
        });
    }

    // Get menu item by ID
    public void getMenuItemById(int id, DataCallback<MenuItem> callback) {
        executorService.execute(() -> {
            try {
                MenuItem item = menuItemDao.getMenuItemById(id);
                callback.onSuccess(item);
            } catch (Exception e) {
                callback.onError("Error fetching menu item: " + e.getMessage());
            }
        });
    }

    // Get menu items by category
    public void getMenuItemsByCategory(String category, DataCallback<List<MenuItem>> callback) {
        executorService.execute(() -> {
            try {
                List<MenuItem> items = menuItemDao.getMenuItemsByCategory(category);
                callback.onSuccess(items);
            } catch (Exception e) {
                callback.onError("Error fetching menu items by category: " + e.getMessage());
            }
        });
    }

    // Search menu items
    public void searchMenuItems(String query, DataCallback<List<MenuItem>> callback) {
        executorService.execute(() -> {
            try {
                List<MenuItem> items = menuItemDao.searchMenuItems(query);
                callback.onSuccess(items);
            } catch (Exception e) {
                callback.onError("Error searching menu items: " + e.getMessage());
            }
        });
    }

    // Insert menu item
    public void insertMenuItem(MenuItem menuItem, DataCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                long id = menuItemDao.insert(menuItem);
                callback.onSuccess(id);
            } catch (Exception e) {
                callback.onError("Error inserting menu item: " + e.getMessage());
            }
        });
    }

    // Update menu item
    public void updateMenuItem(MenuItem menuItem, DataCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                menuItemDao.update(menuItem);
                callback.onSuccess(true);
            } catch (Exception e) {
                callback.onError("Error updating menu item: " + e.getMessage());
            }
        });
    }

    // Delete menu item
    public void deleteMenuItem(MenuItem menuItem, DataCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                menuItemDao.delete(menuItem);
                callback.onSuccess(true);
            } catch (Exception e) {
                callback.onError("Error deleting menu item: " + e.getMessage());
            }
        });
    }

    // Initialize with sample data
    public void initializeSampleData(DataCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                // Check if data already exists
                List<MenuItem> existing = menuItemDao.getAllMenuItems();
                if (existing.isEmpty()) {
                    // Add sample menu items
                    menuItemDao.insert(new MenuItem("Burger", "Juicy, flame-grilled beef patty topped with melted cheddar, lettuce, and tomato.", 10.00, "", "Main Course"));
                    menuItemDao.insert(new MenuItem("Salad", "A crisp blend of greens, cucumber, and cherry tomatoes with dressing.", 8.00, "", "Appetizer"));
                    menuItemDao.insert(new MenuItem("Pizza Margherita", "Baked with tomato sauce, mozzarella, and fresh basil.", 12.00, "", "Main Course"));
                    menuItemDao.insert(new MenuItem("Pasta Carbonara", "Creamy pasta with bacon, egg, and parmesan cheese.", 11.00, "", "Main Course"));
                    menuItemDao.insert(new MenuItem("Chocolate Cake", "Rich, moist chocolate cake with chocolate frosting.", 6.00, "", "Dessert"));
                    menuItemDao.insert(new MenuItem("Ice Cream", "Vanilla ice cream with your choice of toppings.", 5.00, "", "Dessert"));
                }
                callback.onSuccess(true);
            } catch (Exception e) {
                callback.onError("Error initializing sample data: " + e.getMessage());
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
