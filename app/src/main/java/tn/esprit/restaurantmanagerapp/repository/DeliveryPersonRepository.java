package tn.esprit.restaurantmanagerapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tn.esprit.restaurantmanagerapp.dao.DeliveryPersonDao;
import tn.esprit.restaurantmanagerapp.database.AppDatabase;
import tn.esprit.restaurantmanagerapp.entity.DeliveryPerson;

public class DeliveryPersonRepository {

    private final DeliveryPersonDao deliveryPersonDao;
    private final ExecutorService executorService;

    // Constructor that initializes the repository with a context
    public DeliveryPersonRepository(Context context) {
        // Get the AppDatabase instance
        AppDatabase db = AppDatabase.getInstance(context);
        // Initialize the DAO
        this.deliveryPersonDao = db.DeliveryPersonDao();  // Use the correct method for getting the DAO
        // Create an ExecutorService with a fixed thread pool
        this.executorService = Executors.newFixedThreadPool(2); // Allows multiple concurrent tasks
    }

    // Insert a delivery person into the database
    public void insertDeliveryPerson(DeliveryPerson deliveryPerson) {
        executorService.execute(() -> {
            try {
                deliveryPersonDao.insertDeliveryPerson(deliveryPerson);
            } catch (Exception e) {
                e.printStackTrace();  // Add proper logging if necessary
            }
        });
    }

    // Update a delivery person in the database
    public void updateDeliveryPerson(DeliveryPerson deliveryPerson) {
        executorService.execute(() -> {
            try {
                deliveryPersonDao.updateDeliveryPerson(deliveryPerson);
            } catch (Exception e) {
                e.printStackTrace();  // Add proper logging if necessary
            }
        });
    }

    // Delete a delivery person from the database
    public void deleteDeliveryPerson(DeliveryPerson deliveryPerson) {
        executorService.execute(() -> {
            try {
                deliveryPersonDao.deleteDeliveryPerson(deliveryPerson);
            } catch (Exception e) {
                e.printStackTrace();  // Add proper logging if necessary
            }
        });
    }

    // Retrieve a delivery person by ID
    public LiveData<DeliveryPerson> getDeliveryPersonById(int id) {
        return deliveryPersonDao.getDeliveryPersonById(id);
    }

    // Retrieve all delivery persons from the database
    public LiveData<List<DeliveryPerson>> getAllDeliveryPersons() {
        return deliveryPersonDao.getAllDeliveryPersons();
    }

    // Find delivery persons by name
    public LiveData<List<DeliveryPerson>> findDeliveryPersonsByName(String name) {
        return deliveryPersonDao.findDeliveryPersonsByName(name);
    }
}
