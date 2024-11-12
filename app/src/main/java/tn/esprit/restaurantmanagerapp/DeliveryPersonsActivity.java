package tn.esprit.restaurantmanagerapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tn.esprit.restaurantmanagerapp.adapter.DeliveryPersonAdapter;
import tn.esprit.restaurantmanagerapp.entity.DeliveryPerson;
import tn.esprit.restaurantmanagerapp.repository.DeliveryPersonRepository;

public class DeliveryPersonsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeliveryPersonAdapter adapter;
    private DeliveryPersonRepository deliveryPersonRepository;
    private EditText searchEditText;
    private Spinner ratingSpinner;
    private List<DeliveryPerson> deliveryPersonList = new ArrayList<>(); // Holds the full list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_persons);

        recyclerView = findViewById(R.id.delivery_persons_recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
        ratingSpinner = findViewById(R.id.rating_spinner);

        // Initialize DeliveryPersonRepository with context
        deliveryPersonRepository = new DeliveryPersonRepository(this);

        // Initialize the RecyclerView and Adapter
        adapter = new DeliveryPersonAdapter(this, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Observe the data from the repository
        deliveryPersonRepository.getAllDeliveryPersons().observe(this, new Observer<List<DeliveryPerson>>() {
            @Override
            public void onChanged(List<DeliveryPerson> deliveryPersons) {
                if (deliveryPersons == null || deliveryPersons.isEmpty()) {
                    insertDummyData();  // Insert dummy data if the list is empty
                } else {
                    deliveryPersonList = deliveryPersons;
                    filterDeliveryPersons();
                }
            }
        });

        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDeliveryPersons();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        // Set up filter by rating functionality
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.ratings_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(spinnerAdapter);

        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterDeliveryPersons();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not needed
            }
        });
    }

    private void filterDeliveryPersons() {
        String query = searchEditText.getText().toString().trim().toLowerCase();
        String ratingString = ratingSpinner.getSelectedItem().toString();

        List<DeliveryPerson> filteredList = new ArrayList<>(deliveryPersonList);

        // Filter by search query
        if (!query.isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(dp -> dp.getName().toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }

        // Filter by rating
        if (!ratingString.equals("All")) {
            int rating = Integer.parseInt(ratingString);
            filteredList = filteredList.stream()
                    .filter(dp -> dp.getRating() >= rating)
                    .collect(Collectors.toList());
        }

        // Update adapter with the filtered list
        adapter.setDeliveryPersons(filteredList);
    }

    private void insertDummyData() {
        // Insert some dummy data if the list is empty
        DeliveryPerson dp1 = new DeliveryPerson("Ahmed", "ahmed@example.com", "1234567890", "Toyota Corolla", 4);
        DeliveryPerson dp2 = new DeliveryPerson("Fatma", "fatma@example.com", "0987654321", "Honda Civic", 5);
        DeliveryPerson dp3 = new DeliveryPerson("Mohamed", "mohamed@example.com", "1122334455", "Ford Focus", 3);

        deliveryPersonRepository.insertDeliveryPerson(dp1);
        deliveryPersonRepository.insertDeliveryPerson(dp2);
        deliveryPersonRepository.insertDeliveryPerson(dp3);
    }
}
