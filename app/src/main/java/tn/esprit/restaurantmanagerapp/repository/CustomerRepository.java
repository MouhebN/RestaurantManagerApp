package tn.esprit.restaurantmanagerapp.repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tn.esprit.restaurantmanagerapp.dao.CustomerDao;
import tn.esprit.restaurantmanagerapp.database.AppDatabase;
import tn.esprit.restaurantmanagerapp.entity.Customer;

public class CustomerRepository {
    private final CustomerDao customerDao;
    private final ExecutorService executorService;

    public CustomerRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        customerDao = db.customerDao();
        executorService = Executors.newSingleThreadExecutor(); // Creates a single background thread
    }

    public void insertCustomer(Customer customer) {
        executorService.execute(() -> customerDao.insertCustomer(customer)); // Executes the insertion in the background
    }

    public void updateCustomer(Customer customer) {
        executorService.execute(() -> customerDao.updateCustomer(customer));
    }

    public void deleteCustomer(Customer customer) {
        executorService.execute(() -> customerDao.deleteCustomer(customer));
    }

    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    public Customer getCustomerById(int customerId) {
        return customerDao.getCustomerById(customerId);
    }

    public Customer getCustomerByEmail(String email) {
        return customerDao.getCustomerByEmail(email);
    }

    public int checkEmailExists(String email) {
        return customerDao.checkEmailExists(email);
    }
}
