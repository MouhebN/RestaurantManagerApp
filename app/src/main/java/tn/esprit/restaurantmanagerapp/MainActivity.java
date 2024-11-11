package tn.esprit.restaurantmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Setup for window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the buttons by ID
        Button buttonGoToAddMenu = findViewById(R.id.buttonGoToAddMenu);
        Button buttonGoToDelivery = findViewById(R.id.buttonGoToDelivery);

        // Set onClickListener for Go to Add Menu
        buttonGoToAddMenu.setOnClickListener(v -> {
            // Start AddMenuItemActivity
            Intent intent = new Intent(MainActivity.this, AddMenuItemActivity.class);
            startActivity(intent);
        });

        // Set onClickListener for Go to Delivery
        buttonGoToDelivery.setOnClickListener(v -> {
            // Start DeliveryActivity
            Intent intent = new Intent(MainActivity.this, DeliveryActivity.class);
            startActivity(intent);
        });
    }
}
