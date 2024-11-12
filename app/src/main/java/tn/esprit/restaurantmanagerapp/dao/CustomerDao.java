package tn.esprit.restaurantmanagerapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.OnConflictStrategy;

import java.util.List;

import tn.esprit.restaurantmanagerapp.entity.Customer;

@Dao
public interface CustomerDao {

    // Insert a new customer, ignoring if email already exists
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCustomer(Customer customer);

    // Fetch a customer by their ID
    @Query("SELECT * FROM customers WHERE customer_id = :id")
    Customer getCustomerById(int id);

    // Fetch all customers
    @Query("SELECT * FROM customers")
    List<Customer> getAllCustomers();

    // Update customer details
    @Update
    void updateCustomer(Customer customer);

    // Delete a customer
    @Delete
    void deleteCustomer(Customer customer);

    // Fetch a customer by email (useful for authentication)
    @Query("SELECT * FROM customers WHERE email = :email")
    Customer getCustomerByEmail(String email);

    // Fetch a customer by email and password (for login authentication)
    @Query("SELECT * FROM customers WHERE email = :email AND password = :password")
    Customer getCustomerByEmailAndPassword(String email, String password);

    // Check if an email exists in the database
    @Query("SELECT COUNT(*) FROM customers WHERE email = :email")
    int checkEmailExists(String email);

    // Fetch customers with a specific name (example for filtering)
    @Query("SELECT * FROM customers WHERE name = :name")
    List<Customer> getCustomersByName(String name);

    // Delete customer by email
    @Query("DELETE FROM customers WHERE email = :email")
    void deleteCustomerByEmail(String email);



}
