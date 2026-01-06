package com.example.dineeasy.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dineeasy.database.entities.MenuItem;

import java.util.List;

@Dao
public interface MenuItemDao {

    @Insert
    long insert(MenuItem menuItem);

    @Update
    void update(MenuItem menuItem);

    @Delete
    void delete(MenuItem menuItem);

    @Query("SELECT * FROM menu_items ORDER BY category, name")
    List<MenuItem> getAllMenuItems();

    @Query("SELECT * FROM menu_items WHERE id = :id")
    MenuItem getMenuItemById(int id);

    @Query("SELECT * FROM menu_items WHERE category = :category ORDER BY name")
    List<MenuItem> getMenuItemsByCategory(String category);

    @Query("SELECT * FROM menu_items WHERE name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    List<MenuItem> searchMenuItems(String searchQuery);

    @Query("DELETE FROM menu_items")
    void deleteAll();
}
