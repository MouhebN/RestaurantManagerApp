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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonGoToOrder = findViewById(R.id.buttonGoToOrder);

        buttonGoToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        Button buttonGoToAddMenu = findViewById(R.id.buttonGoToAddMenu);

        buttonGoToAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMenuItemActivity.class);
                startActivity(intent);
            }
        });
        Button buttonGoToAddReservation = findViewById(R.id.buttonGoToAddReservation);
        buttonGoToAddReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddReservationActivity.class);
                startActivity(intent);
            }
        });

        Button buttonGoToDelivery = findViewById(R.id.buttonGoToDelivery);
        buttonGoToDelivery.setOnClickListener(v -> {
            // Start DeliveryActivity
            Intent intent = new Intent(MainActivity.this, DeliveryActivity.class);
            startActivity(intent);
        });
    }
}
