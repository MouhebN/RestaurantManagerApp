package tn.esprit.restaurantmanagerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tn.esprit.restaurantmanagerapp.entity.MenuItem;
import tn.esprit.restaurantmanagerapp.repository.MenuItemRepository;

public class AddMenuItemActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextPrice, editTextCategory;
    private Button buttonAddMenuItem;
    private MenuItemRepository menuItemRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        // Initialize Views
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextCategory = findViewById(R.id.editTextCategory);
        buttonAddMenuItem = findViewById(R.id.buttonAddMenuItem);


        menuItemRepository = new MenuItemRepository(getApplicationContext());

        // Set OnClickListener for the button
        buttonAddMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenuItem();
            }
        });
    }

    private void addMenuItem() {
        // Get input values
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String priceText = editTextPrice.getText().toString();
        String category = editTextCategory.getText().toString();

        // Validate inputs
        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse price
        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new MenuItem
        MenuItem menuItem = new MenuItem(name, description, price, category);

        // Insert item in the background
        menuItemRepository.insertMenuItem(menuItem);

        // Notify user
        Toast.makeText(this, "Menu item added successfully", Toast.LENGTH_SHORT).show();

        // Clear input fields
        editTextName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextCategory.setText("");
    }
}
