package tn.esprit.restaurantmanagerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

        // Définir l'adaptateur pour RecyclerView
        recyclerViewDeliveryItems.setAdapter(deliveryAdapter);

        // Charger les livraisons
        loadDeliveries();

        // Initialiser le bouton "Ajouter une livraison"
        buttonAddDelivery = findViewById(R.id.buttonAddNewDelivery);

        // Rediriger vers l'interface d'ajout
        buttonAddDelivery.setOnClickListener(v -> {
            Intent intent = new Intent(DeliveryListActivity.this, DeliveryActivity.class);
            startActivity(intent);
        });

    }



    private void loadDeliveries() {
        deliveryRepository.getAllDeliveries(new DeliveryRepository.DeliveriesCallback() {
            @Override
            public void onDeliveriesLoaded(List<Delivery    > deliveries) {
                if (deliveries != null && !deliveries.isEmpty()) {
                    deliveryAdapter.updateData(deliveries);
                } else {
                    Toast.makeText(DeliveryListActivity.this, "Aucune livraison trouvée", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadDeliveries(); // Recharger la liste des livraisons à chaque fois que l'activité est au premier plan
    }



}