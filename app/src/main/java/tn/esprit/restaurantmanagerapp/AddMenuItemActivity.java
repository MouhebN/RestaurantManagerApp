package tn.esprit.restaurantmanagerapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
    private List<MenuItem> filteredMenuItems = new ArrayList<>();

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
        EditText editTextSearch = findViewById(R.id.editTextSearch);

        menuItemRepository = new MenuItemRepository(getApplicationContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerCategory.setAdapter(adapter);

        // Load menu items from the repository
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

                // Apply filtering initially (in case there’s already a search or category selection)
                filterMenuItems(editTextSearch.getText().toString());
            }
        });

        // Set OnClickListener for the button
        buttonAddMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenuItem();
            }
        });

        // Set up search functionality with TextWatcher
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMenuItems(s.toString()); // Filter items as user types
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set a listener on the category spinner to filter items when category changes
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterMenuItems(editTextSearch.getText().toString()); // Re-filter based on search text and selected category
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                filterMenuItems(editTextSearch.getText().toString()); // Default filter when nothing is selected
            }
        });
    }

    // Method to filter menu items based on search query and selected category
    private void filterMenuItems(String query) {
        String selectedCategory = spinnerCategory.getSelectedItem().toString();
        filteredMenuItems.clear();

        for (MenuItem item : menuItems) {
            boolean matchesCategory = selectedCategory.equals("All") || item.getCategory().equals(selectedCategory);
            boolean matchesSearch = item.getName().toLowerCase().contains(query.toLowerCase());

            if (matchesCategory && matchesSearch) {
                filteredMenuItems.add(item);
            }
        }

        menuItemAdapter.updateMenuItems(filteredMenuItems); // Update the adapter with the filtered list
    }

    // Method to add a new menu item
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

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        MenuItem menuItem = new MenuItem(name, description, price, category);

        menuItemRepository.insertMenuItem(menuItem);

        menuItems.add(menuItem);
        menuItemAdapter.notifyItemInserted(menuItems.size() - 1);

        Toast.makeText(this, "Menu item added successfully", Toast.LENGTH_SHORT).show();

        // Clear the input fields
        editTextName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        spinnerCategory.setSelection(0);
    }
}
