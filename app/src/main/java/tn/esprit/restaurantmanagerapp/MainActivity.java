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

        // Setup for window insets to handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // Initialize buttons and set onClick listeners
        initializeButton(R.id.buttonGoToOrder, OrderActivity.class);
        initializeButton(R.id.buttonGoToAddMenu, AddMenuItemActivity.class);
        initializeButton(R.id.buttonGoToAddReservation, AddReservationActivity.class);
    }

    /**
     * Initializes a button with a click listener to start a specified activity.
     *
     * @param buttonId the ID of the button to initialize
     * @param activityClass the class of the activity to start on button click
     */
    private void initializeButton(int buttonId, Class<?> activityClass) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activityClass);
            startActivity(intent);
        });
    }
}
