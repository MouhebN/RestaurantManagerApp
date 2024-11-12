package tn.esprit.restaurantmanagerapp.repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tn.esprit.restaurantmanagerapp.dao.OrderDao;
import tn.esprit.restaurantmanagerapp.database.AppDatabase;
import tn.esprit.restaurantmanagerapp.entity.Order;

public class OrderRepository {
    private final OrderDao orderDao;
    private final ExecutorService executorService;

    public OrderRepository(Context context) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        orderDao = db.orderDao();
        executorService = Executors.newSingleThreadExecutor(); // Background thread for database operations
    }

    public void insertOrder(Order order) {
        executorService.execute(() -> orderDao.insertOrder(order));
    }

    public void updateOrder(Order order) {
        executorService.execute(() -> orderDao.updateOrder(order));
    }

    public void deleteOrder(Order order) {
        executorService.execute(() -> orderDao.deleteOrder(order));
    }

    public void getAllOrders(OrdersCallback callback) {
        executorService.execute(() -> {
            List<Order> orders = orderDao.getAllOrders();
            callback.onOrdersLoaded(orders); // Pass data to callback after loading
        });
    }

    public interface OrdersCallback {
        void onOrdersLoaded(List<Order> orders);
    }
}
