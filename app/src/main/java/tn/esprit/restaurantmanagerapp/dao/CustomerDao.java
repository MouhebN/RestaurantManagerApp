package tn.esprit.restaurantmanagerapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

import tn.esprit.restaurantmanagerapp.entity.Customer;

@Dao
public interface CustomerDao {

    @Insert
    void insertCustomer(Customer customer);

    @Query("SELECT * FROM customers WHERE customerId = :id")
    Customer getCustomerById(int id);

    @Query("SELECT * FROM customers")
    List<Customer> getAllCustomers();

    @Update
    void updateCustomer(Customer customer);

    @Delete
    void deleteCustomer(Customer customer);
}
