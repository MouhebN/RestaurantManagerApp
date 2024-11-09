package tn.esprit.restaurantmanagerapp.repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tn.esprit.restaurantmanagerapp.dao.MenuDao;
import tn.esprit.restaurantmanagerapp.database.AppDatabase;
import tn.esprit.restaurantmanagerapp.entity.MenuItem;

public class MenuItemRepository {
    private final MenuDao menuDao;
    private final ExecutorService executorService;

    public MenuItemRepository(Context context) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        menuDao = db.menuDao();
        executorService = Executors.newSingleThreadExecutor(); // Creates a single background thread
    }

    public void insertMenuItem(MenuItem menuItem) {
        executorService.execute(() -> menuDao.insertMenuItem(menuItem)); // Executes the insertion in the background
    }

    public void updateMenuItem(MenuItem menuItem) {
        executorService.execute(() -> menuDao.updateMenuItem(menuItem));
    }

    public void deleteMenuItem(MenuItem menuItem) {
        executorService.execute(() -> menuDao.deleteMenuItem(menuItem));
    }

    public void getAllMenuItems(MenuItemsCallback callback) {
        executorService.execute(() -> {
            List<MenuItem> menuItems = menuDao.getAllMenuItems();
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                callback.onMenuItemsLoaded(menuItems);
            });
        });
    }

    public MenuItem getMenuItemById(int id) {
        return menuDao.getMenuItemById(id);
    }

    public interface MenuItemsCallback {
        void onMenuItemsLoaded(List<MenuItem> menuItems);
    }
}


