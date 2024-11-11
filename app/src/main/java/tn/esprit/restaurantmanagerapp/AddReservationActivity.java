package tn.esprit.restaurantmanagerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.restaurantmanagerapp.adapter.ReservationAdapter;
import tn.esprit.restaurantmanagerapp.entity.Reservation;
import tn.esprit.restaurantmanagerapp.repository.ReservationRepository;

public class AddReservationActivity extends AppCompatActivity {

    private EditText editTextCustomerId, editTextReservationDate, editTextReservationTime, editTextPartySize;
    private Button buttonAddReservation;
    private ReservationRepository reservationRepository;
    private RecyclerView recyclerViewReservations;
    private ReservationAdapter reservationAdapter;
    private List<Reservation> reservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);
        reservations = new ArrayList<>();

        // Initialize Views
        editTextCustomerId = findViewById(R.id.editTextCustomerId);
        editTextReservationDate = findViewById(R.id.editTextReservationDate);
        editTextReservationTime = findViewById(R.id.editTextReservationTime);
        editTextPartySize = findViewById(R.id.editTextPartySize);
        buttonAddReservation = findViewById(R.id.buttonAddReservation);
        recyclerViewReservations = findViewById(R.id.recyclerViewReservations);

        // Initialize the repository
        reservationRepository = new ReservationRepository(getApplicationContext());

        // Load existing reservations and setup RecyclerView
        reservationRepository.getAllReservations(new ReservationRepository.ReservationsCallback() {
            @Override
            public void onReservationsLoaded(List<Reservation> loadedReservations) {
                if (loadedReservations == null) {
                    reservations = new ArrayList<>();
                } else {
                    reservations = loadedReservations;
                }

                // Set up RecyclerView
                recyclerViewReservations.setLayoutManager(new LinearLayoutManager(AddReservationActivity.this));
                reservationAdapter = new ReservationAdapter(reservations, reservationRepository, AddReservationActivity.this);
                recyclerViewReservations.setAdapter(reservationAdapter);
            }
        });

        // Set OnClickListener for the button
        buttonAddReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReservation();
            }
        });
    }

    private void addReservation() {
        // Get input values
        String customerIdText = editTextCustomerId.getText().toString();
        String reservationDate = editTextReservationDate.getText().toString();
        String reservationTime = editTextReservationTime.getText().toString();
        String partySizeText = editTextPartySize.getText().toString();

        if (customerIdText.isEmpty() || reservationDate.isEmpty() || reservationTime.isEmpty() || partySizeText.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse inputs
        int customerId;
        int partySize;
        try {
            customerId = Integer.parseInt(customerIdText);
            partySize = Integer.parseInt(partySizeText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid format for Customer ID or Party Size", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new Reservation
        Reservation reservation = new Reservation(customerId, reservationDate, reservationTime, partySize);

        // Insert reservation in the background
        reservationRepository.insertReservation(reservation);

        // Update the RecyclerView
        reservations.add(reservation);
        reservationAdapter.notifyItemInserted(reservations.size() - 1);

        Toast.makeText(this, "Reservation added successfully", Toast.LENGTH_SHORT).show();

        // Clear input fields
        editTextCustomerId.setText("");
        editTextReservationDate.setText("");
        editTextReservationTime.setText("");
        editTextPartySize.setText("");
    }
}
