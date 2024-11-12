package tn.esprit.restaurantmanagerapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

import tn.esprit.restaurantmanagerapp.entity.Delivery;

@Dao
public interface DeliveryDao {

    @Insert
    void insertDelivery(Delivery delivery);

    @Query("SELECT * FROM deliveries WHERE deliveryId = :id")
    Delivery getDeliveryById(int id);

    @Query("SELECT * FROM deliveries")
    List<Delivery> getAllDeliveries();

    @Update
    void updateDelivery(Delivery delivery);

    @Delete
    void deleteDelivery(Delivery delivery);
}
