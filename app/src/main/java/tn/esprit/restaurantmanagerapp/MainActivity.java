package tn.esprit.restaurantmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // Find the button by ID
        Button buttonGoToOrder = findViewById(R.id.buttonGoToOrder);

        // Set an onClickListener
        buttonGoToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start OrderActivity
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
        // Find the button by ID
        Button buttonGoToAddMenu = findViewById(R.id.buttonGoToAddMenu);

        // Set an onClickListener
        buttonGoToAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddMenuItemActivity
                Intent intent = new Intent(MainActivity.this, AddMenuItemActivity.class);
                startActivity(intent);
            }
        });
        Button buttonGoToAddReservation = findViewById(R.id.buttonGoToAddReservation);
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
