package tn.esprit.restaurantmanagerapp.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tn.esprit.restaurantmanagerapp.dao.DeliveryDao;
import tn.esprit.restaurantmanagerapp.database.AppDatabase;
import tn.esprit.restaurantmanagerapp.entity.Delivery;

public class DeliveryRepository {
    private final DeliveryDao deliveryDao;
    private final ExecutorService executorService;

    public DeliveryRepository(Context context) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        deliveryDao = db.deliveryDao();
        executorService = Executors.newSingleThreadExecutor(); // Background thread for database operations
    }

    public void insertDelivery(Delivery delivery) {
        executorService.execute(() -> deliveryDao.insertDelivery(delivery));
    }

    public void updateDelivery(Delivery delivery) {
        executorService.execute(() -> deliveryDao.updateDelivery(delivery));
    }

    public void deleteDelivery(Delivery delivery) {
        executorService.execute(() -> deliveryDao.deleteDelivery(delivery));
    }

    public void getAllDeliveries(DeliveriesCallback callback) {
        executorService.execute(() -> {
            List<Delivery> deliveries = deliveryDao.getAllDeliveries();
            new Handler(Looper.getMainLooper()).post(() -> callback.onDeliveriesLoaded(deliveries));
        });
    }

    public void getDeliveryById(int id, DeliveryCallback callback) {
        executorService.execute(() -> {
            Delivery delivery = deliveryDao.getDeliveryById(id);
            new Handler(Looper.getMainLooper()).post(() -> callback.onDeliveryLoaded(delivery));
        });
    }

    // Callback interface for loading multiple deliveries
    public interface DeliveriesCallback {
        void onDeliveriesLoaded(List<Delivery> deliveries);
    }

    // Callback interface for loading a single delivery by ID
    public interface DeliveryCallback {
        void onDeliveryLoaded(Delivery delivery);
    }
}