package tn.esprit.restaurantmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup for window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the buttons by ID
        Button buttonGoToAddMenu = findViewById(R.id.buttonGoToAddMenu);
        Button buttonGoToAddReservation = findViewById(R.id.buttonGoToAddReservation);

        // Set onClickListener for "Go to Add Menu Item"
        buttonGoToAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddMenuItemActivity
                Intent intent = new Intent(MainActivity.this, AddMenuItemActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for "Go to Add Reservation"
        buttonGoToAddReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddReservationActivity
                Intent intent = new Intent(MainActivity.this, AddReservationActivity.class);
                startActivity(intent);
            }
        });
    }
}
