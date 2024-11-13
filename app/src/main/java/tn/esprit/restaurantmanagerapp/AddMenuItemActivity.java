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
    private EditText editTextName, editTextDescription, editTextPrice, editTextSearch;
    private Button buttonAddMenuItem;
    private MenuItemRepository menuItemRepository;
    private RecyclerView recyclerViewMenuItems;
    private MenuItemAdapter menuItemAdapter;
    private List<MenuItem> menuItems = new ArrayList<>();
    private List<MenuItem> filteredMenuItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        initializeViews();
        setupRecyclerView();
        setupSpinner();
        loadMenuItems();
        setupSearchListener();
    }

    private void initializeViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextSearch = findViewById(R.id.editTextSearch);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonAddMenuItem = findViewById(R.id.buttonAddMenuItem);
        buttonAddMenuItem.setOnClickListener(v -> addMenuItem());
        menuItemRepository = new MenuItemRepository(getApplicationContext());
    }

    private void setupRecyclerView() {
        recyclerViewMenuItems = findViewById(R.id.recyclerViewMenuItems);
        recyclerViewMenuItems.setLayoutManager(new LinearLayoutManager(this));
        menuItemAdapter = new MenuItemAdapter(menuItems, menuItemRepository, this);
        recyclerViewMenuItems.setAdapter(menuItemAdapter);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterMenuItems(editTextSearch.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterMenuItems(editTextSearch.getText().toString());
            }
        });
    }

    private void loadMenuItems() {
        menuItemRepository.getAllMenuItems(loadedMenuItems -> {
            if (loadedMenuItems != null) {
                menuItems.clear();
                menuItems.addAll(loadedMenuItems);
                filterMenuItems(editTextSearch.getText().toString());
            } else {
                Toast.makeText(this, "Error loading menu items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearchListener() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMenuItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

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
        menuItemAdapter.updateMenuItems(filteredMenuItems);
    }

    private void addMenuItem() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceText = editTextPrice.getText().toString().trim();
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

        // Avoid duplicates
        for (MenuItem item : menuItems) {
            if (item.getName().equalsIgnoreCase(name) && item.getDescription().equalsIgnoreCase(description)) {
                Toast.makeText(this, "Item already exists", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        MenuItem menuItem = new MenuItem(name, description, price, category);
        menuItemRepository.insertMenuItem(menuItem);
        menuItems.add(menuItem);
        menuItemAdapter.notifyItemInserted(menuItems.size() - 1);

        Toast.makeText(this, "Menu item added successfully", Toast.LENGTH_SHORT).show();
        clearInputFields();
    }

    private void clearInputFields() {
        editTextName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        spinnerCategory.setSelection(0);
    }
}
