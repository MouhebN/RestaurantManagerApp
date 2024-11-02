package tn.esprit.restaurantmanagerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.restaurantmanagerapp.adapter.MenuItemAdapter;
import tn.esprit.restaurantmanagerapp.entity.MenuItem;
import tn.esprit.restaurantmanagerapp.repository.MenuItemRepository;


public class AddMenuItemActivity extends AppCompatActivity {
    private Spinner spinnerCategory;
    private EditText editTextName, editTextDescription, editTextPrice, editTextCategory;
    private Button buttonAddMenuItem;
    private MenuItemRepository menuItemRepository;
    private RecyclerView recyclerViewMenuItems;
    private MenuItemAdapter menuItemAdapter;
    private List<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);
        menuItems = new ArrayList<>();

        // Initialize Views
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonAddMenuItem = findViewById(R.id.buttonAddMenuItem);
        recyclerViewMenuItems = findViewById(R.id.recyclerViewMenuItems);

        // Initialize the repository
        menuItemRepository = new MenuItemRepository(getApplicationContext());

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerCategory.setAdapter(adapter);

        menuItemRepository.getAllMenuItems(new MenuItemRepository.MenuItemsCallback() {
            @Override
            public void onMenuItemsLoaded(List<MenuItem> loadedMenuItems) {
                if (loadedMenuItems == null) {
                    menuItems = new ArrayList<>();
                } else {
                    menuItems = loadedMenuItems;
                }

                // Set up RecyclerView
                recyclerViewMenuItems.setLayoutManager(new LinearLayoutManager(AddMenuItemActivity.this));
                menuItemAdapter = new MenuItemAdapter(menuItems, menuItemRepository, AddMenuItemActivity.this);
                recyclerViewMenuItems.setAdapter(menuItemAdapter);
            }
        });

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
        String category = spinnerCategory.getSelectedItem().toString();

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

        // Update the RecyclerView
        menuItems.add(menuItem);
        menuItemAdapter.notifyItemInserted(menuItems.size() - 1);

        Toast.makeText(this, "Menu item added successfully", Toast.LENGTH_SHORT).show();

        // Clear input fields
        editTextName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        spinnerCategory.setSelection(0);
    }

}
