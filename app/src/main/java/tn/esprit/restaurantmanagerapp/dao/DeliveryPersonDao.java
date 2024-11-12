package tn.esprit.restaurantmanagerapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tn.esprit.restaurantmanagerapp.entity.DeliveryPerson;

@Dao
public interface DeliveryPersonDao {

    @Insert
    void insertDeliveryPerson(DeliveryPerson deliveryPerson);

    @Update
    void updateDeliveryPerson(DeliveryPerson deliveryPerson);

    @Delete
    void deleteDeliveryPerson(DeliveryPerson deliveryPerson);

    @Query("SELECT * FROM delivery_persons WHERE delivery_person_id = :id")
    LiveData<DeliveryPerson> getDeliveryPersonById(int id);

    @Query("SELECT * FROM delivery_persons")
    LiveData<List<DeliveryPerson>> getAllDeliveryPersons();

    @Query("SELECT * FROM delivery_persons WHERE name LIKE :name")
    LiveData<List<DeliveryPerson>> findDeliveryPersonsByName(String name);
}
