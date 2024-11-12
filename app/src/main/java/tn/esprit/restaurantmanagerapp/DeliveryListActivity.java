package tn.esprit.restaurantmanagerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class DeliveryListActivity extends AppCompatActivity {

    private DeliveryRepository deliveryRepository;
    private RecyclerView recyclerViewDeliveryItems;
    private DeliveryAdapter deliveryAdapter;
    private Button buttonAddDelivery;
    private Spinner triDelivery;
    private List<Delivery> deliveryList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_list);

        // Initialiser RecyclerView
        recyclerViewDeliveryItems = findViewById(R.id.recyclerViewDeliveryItems);
        recyclerViewDeliveryItems.setLayoutManager(new LinearLayoutManager(this));

        // Initialiser DeliveryRepository et Adapter
        deliveryRepository = new DeliveryRepository(this);
        deliveryAdapter = new DeliveryAdapter(this, new ArrayList<>(), deliveryRepository);
        recyclerViewDeliveryItems.setAdapter(deliveryAdapter);

        // Initialiser le Spinner pour le tri
        triDelivery = findViewById(R.id.triDelivery);
        setupSpinner();

        // Initialiser le bouton "Ajouter une livraison"
        buttonAddDelivery = findViewById(R.id.buttonAddNewDelivery);
        buttonAddDelivery.setOnClickListener(v -> {
            Intent intent = new Intent(DeliveryListActivity.this, DeliveryActivity.class);
            startActivity(intent);
        });

        // Charger les livraisons
        loadDeliveries();
    }

    // Méthode pour configurer le Spinner
    private void setupSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Tous", "En cours", "Distribuee", "Annulee"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        triDelivery.setAdapter(spinnerAdapter);

        // Gérer la sélection du Spinner
        triDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = (String) parent.getItemAtPosition(position);
                filterListByStatus(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ne rien faire
            }
        });
    }

    // Charger la liste des livraisons
    private void loadDeliveries() {
        deliveryRepository.getAllDeliveries(new DeliveryRepository.DeliveriesCallback() {
            @Override
            public void onDeliveriesLoaded(List<Delivery> deliveries) {
                if (deliveries != null && !deliveries.isEmpty()) {
                    deliveryList = deliveries;
                    deliveryAdapter.updateData(deliveries);
                } else {
                    Toast.makeText(DeliveryListActivity.this, "Aucune livraison trouvée", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Méthode pour filtrer la liste selon le statut sélectionné
    private void filterListByStatus(String status) {
        List<Delivery> filteredList = new ArrayList<>();

        if (status.equals("Tous")) {
            filteredList.addAll(deliveryList);
        } else {
            for (Delivery delivery : deliveryList) {
                if (delivery.getDeliveryStatus().equalsIgnoreCase(status)) {
                    filteredList.add(delivery);
                }
            }
        }

        // Mettre à jour l'adaptateur avec la liste filtrée
        deliveryAdapter.updateData(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDeliveries(); // Recharger la liste des livraisons à chaque fois que l'activité est au premier plan
    }
}
