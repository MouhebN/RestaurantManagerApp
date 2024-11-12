package tn.esprit.restaurantmanagerapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.content.Intent;

import tn.esprit.restaurantmanagerapp.adapter.DeliveryAdapter;
import tn.esprit.restaurantmanagerapp.entity.Delivery;
import tn.esprit.restaurantmanagerapp.repository.DeliveryRepository;

public class DeliveryActivity extends AppCompatActivity {

    private EditText editTextOrderId, editTextCustomerId, editTextAddress, editTextStatus;
    private Button buttonAddDelivery;
    private DeliveryRepository deliveryRepository;
    private RecyclerView recyclerViewDeliveryItems;
    private DeliveryAdapter deliveryAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery);

        // Initialiser les champs de saisie et les boutons
        editTextOrderId = findViewById(R.id.editTextOrderId);
        editTextCustomerId = findViewById(R.id.editTextCustomerId);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextStatus = findViewById(R.id.editTextStatus);
        buttonAddDelivery = findViewById(R.id.buttonAddDelivery);

        // Initialiser RecyclerView
        recyclerViewDeliveryItems = findViewById(R.id.recyclerViewDeliveryItems);

        if (recyclerViewDeliveryItems != null) {
            recyclerViewDeliveryItems.setLayoutManager(new LinearLayoutManager(this));
            deliveryAdapter = new DeliveryAdapter(this, new ArrayList<>());
            recyclerViewDeliveryItems.setAdapter(deliveryAdapter);
        } else {
            Toast.makeText(this, "Erreur : RecyclerView introuvable", Toast.LENGTH_SHORT).show();
        }

        // Initialiser DeliveryRepository
        deliveryRepository = new DeliveryRepository(this);

        // Listener pour ajouter une livraison
        buttonAddDelivery.setOnClickListener(v -> addDelivery());

        // Listener pour voir la liste des livraisons
        Button buttonViewDeliveries = findViewById(R.id.buttonViewDeliveries);
        buttonViewDeliveries.setOnClickListener(v -> {
            listDelivery();
        });
    }

    private void listDelivery() {
        Intent intent = new Intent(DeliveryActivity.this, DeliveryListActivity.class);
        startActivity(intent);
    }

    private void addDelivery() {
        String orderIdStr = editTextOrderId.getText().toString();
        String customerIdStr = editTextCustomerId.getText().toString();
        String address = editTextAddress.getText().toString();
        String status = editTextStatus.getText().toString();

        if (orderIdStr.isEmpty() || customerIdStr.isEmpty() || address.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            int customerId = Integer.parseInt(customerIdStr);
            Delivery newDelivery = new Delivery(orderId, customerId, address, status);

            // Insérer la livraison dans la base de données
            deliveryRepository.insertDelivery(newDelivery);

            Toast.makeText(this, "Livraison ajoutée avec succès", Toast.LENGTH_SHORT).show();

            // Ajouter la livraison à la liste et rafraîchir l'adaptateur
            deliveryAdapter.addDelivery(newDelivery);
            deliveryAdapter.notifyDataSetChanged();

            // Réinitialiser les champs
            editTextOrderId.setText("");
            editTextCustomerId.setText("");
            editTextAddress.setText("");
            editTextStatus.setText("");

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer des numéros valides pour Order ID et Customer ID", Toast.LENGTH_SHORT).show();
        }
    }

}