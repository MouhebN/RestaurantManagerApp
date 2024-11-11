package tn.esprit.restaurantmanagerapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.restaurantmanagerapp.adapter.DeliveryAdapter;
import tn.esprit.restaurantmanagerapp.entity.Delivery;
import tn.esprit.restaurantmanagerapp.repository.DeliveryRepository;

public class DeliveryActivity extends AppCompatActivity {

    private EditText editTextOrderId, editTextCustomerId, editTextAddress, editTextStatus;
    private Button buttonAddDelivery;
    private DeliveryRepository deliveryRepository;
    private RecyclerView recyclerViewDeliveryItems;
    private DeliveryAdapter deliveryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery);

        // Initialize the input fields and button
        editTextOrderId = findViewById(R.id.editTextOrderId);
        editTextCustomerId = findViewById(R.id.editTextCustomerId);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextStatus = findViewById(R.id.editTextStatus);
        buttonAddDelivery = findViewById(R.id.buttonAddDelivery);

        // Initialize RecyclerView
        recyclerViewDeliveryItems = findViewById(R.id.recyclerViewDeliveryItems);
        recyclerViewDeliveryItems.setLayoutManager(new LinearLayoutManager(this));

        // Initialize DeliveryRepository and Adapter
        deliveryRepository = new DeliveryRepository(this);
        deliveryAdapter = new DeliveryAdapter(this, new ArrayList<>());
        recyclerViewDeliveryItems.setAdapter(deliveryAdapter);

        // Load deliveries from the database
        loadDeliveries();

        // Set click listener for the "Add Delivery" button
        buttonAddDelivery.setOnClickListener(v -> addDelivery());
    }

    private void loadDeliveries() {
        // Call the repository to fetch deliveries asynchronously
        deliveryRepository.getAllDeliveries(new DeliveryRepository.DeliveriesCallback() {
            @Override
            public void onDeliveriesLoaded(List<Delivery> deliveries) {
                // Update the RecyclerView with the fetched deliveries
                deliveryAdapter.updateData(deliveries);
            }
        });
    }

    private void addDelivery() {
        // Get values from input fields
        String orderIdStr = editTextOrderId.getText().toString();
        String customerIdStr = editTextCustomerId.getText().toString();
        String address = editTextAddress.getText().toString();
        String status = editTextStatus.getText().toString();

        // Validate inputs
        if (orderIdStr.isEmpty() || customerIdStr.isEmpty() || address.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new Delivery object
        int orderId = Integer.parseInt(orderIdStr);
        int customerId = Integer.parseInt(customerIdStr);
        Delivery newDelivery = new Delivery(orderId, customerId, address, status);

        deliveryRepository.insertDelivery(newDelivery);

        Toast.makeText(this, "Delivery added successfully", Toast.LENGTH_SHORT).show();

        editTextOrderId.setText("");
        editTextCustomerId.setText("");
        editTextAddress.setText("");
        editTextStatus.setText("");

        loadDeliveries();
    }
}
