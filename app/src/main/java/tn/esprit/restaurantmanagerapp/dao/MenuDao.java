package tn.esprit.restaurantmanagerapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tn.esprit.restaurantmanagerapp.entity.MenuItem;

@Dao
public interface MenuDao {

    @Insert
    void insertMenuItem(MenuItem menuItem);

    @Query("SELECT * FROM menu_items WHERE menuItemId = :id")
    MenuItem getMenuItemById(int id);

    @Query("SELECT * FROM menu_items")
    List<MenuItem> getAllMenuItems();


    @Update
    void updateMenuItem(MenuItem menuItem);

    @Delete
    void deleteMenuItem(MenuItem menuItem);
}
