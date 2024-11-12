package tn.esprit.restaurantmanagerapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

import tn.esprit.restaurantmanagerapp.entity.Order;

@Dao
public interface OrderDao {

    @Insert
    void insertOrder(Order order);

    @Query("SELECT * FROM orders WHERE orderId = :id")
    Order getOrderById(int id);

    @Query("SELECT * FROM orders")
    List<Order> getAllOrders();

    @Update
    void updateOrder(Order order);

    @Delete
    void deleteOrder(Order order);
}
