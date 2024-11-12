package tn.esprit.restaurantmanagerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import tn.esprit.restaurantmanagerapp.entity.Customer;
import tn.esprit.restaurantmanagerapp.repository.CustomerRepository;

public class DashboardActivity extends AppCompatActivity {

    private EditText usernameTextView, emailTextView, birthdayTextView, locationTextView;
    private ImageView profileImageView;
    private Button updateButton, logoutButton, locationButton, viewDeliveryPersonsButton;
    private CustomerRepository customerRepository;
    private Customer currentUser;
    private ExecutorService executorService;
    private Uri profileImageUri;
    private FusedLocationProviderClient fusedLocationClient;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        usernameTextView = findViewById(R.id.username_text);
        emailTextView = findViewById(R.id.email_text);
        birthdayTextView = findViewById(R.id.birthday_text);
        locationTextView = findViewById(R.id.location_text);
        profileImageView = findViewById(R.id.profile_image);
        updateButton = findViewById(R.id.update_button);
        logoutButton = findViewById(R.id.logout);
        locationButton = findViewById(R.id.get_location_button);
        viewDeliveryPersonsButton = findViewById(R.id.view_all_delivery_persons_button);

        // Initialize CustomerRepository, ExecutorService, and Location Provider
        customerRepository = new CustomerRepository(this);
        executorService = Executors.newSingleThreadExecutor();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set default profile image initially
        profileImageView.setImageResource(R.drawable.img);

        // Retrieve data from intent
        String email = getIntent().getStringExtra("email");
        loadUserData(email);

        // Set button listeners
        updateButton.setOnClickListener(v -> updateCustomerInfo());
        logoutButton.setOnClickListener(v -> logout());
        locationButton.setOnClickListener(v -> fetchCurrentLocation());
        viewDeliveryPersonsButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, DeliveryPersonsActivity.class);
            startActivity(intent);
        });

        // Set click listener on profile image for changing it
        profileImageView.setOnClickListener(v -> openImagePicker());
    }

    private void loadUserData(String email) {
        executorService.execute(() -> {
            currentUser = customerRepository.getCustomerByEmail(email);
            runOnUiThread(() -> {
                if (currentUser != null) {
                    usernameTextView.setText(currentUser.getName());
                    emailTextView.setText(currentUser.getEmail());
                    birthdayTextView.setText(currentUser.getDateOfBirth() != null ? currentUser.getDateOfBirth() : "");
                    locationTextView.setText(currentUser.getLocation() != null ? currentUser.getLocation() : "");

                    // Only set profile image if the user has chosen one
                    if (currentUser.getProfileImageUri() != null && !currentUser.getProfileImageUri().isEmpty()) {
                        try {
                            Uri imageUri = Uri.parse(currentUser.getProfileImageUri());
                            profileImageView.setImageURI(imageUri);
                        } catch (Exception e) {
                            Log.e("DashboardActivity", "Error loading image: " + e.getMessage());
                            profileImageView.setImageResource(R.drawable.img);
                        }
                    }
                } else {
                    Toast.makeText(this, "Error: User data not found.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void updateCustomerInfo() {
        if (currentUser != null) {
            currentUser.setName(usernameTextView.getText().toString());
            currentUser.setLocation(locationTextView.getText().toString());
            currentUser.setEmail(emailTextView.getText().toString());
            currentUser.setDateOfBirth(birthdayTextView.getText().toString());

            if (profileImageUri != null) {
                currentUser.setProfileImageUri(profileImageUri.toString());
            }

            executorService.execute(() -> {
                customerRepository.updateCustomer(currentUser);
                runOnUiThread(() -> Toast.makeText(this, "Customer updated successfully", Toast.LENGTH_SHORT).show());
            });
        } else {
            Toast.makeText(this, "No customer loaded to update", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DashboardActivity.this, SigninActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    profileImageUri = result.getData().getData();
                    profileImageView.setImageURI(profileImageUri);

                    // Update the profile image URI in currentUser and save it to the database
                    if (currentUser != null) {
                        currentUser.setProfileImageUri(profileImageUri.toString());
                        updateCustomerInfo();  // Save the new image URI
                    }
                }
            }
    );

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            String currentLocation = "Lat: " + location.getLatitude() + ", Long: " + location.getLongitude();
                            locationTextView.setText(currentLocation);
                            if (currentUser != null) {
                                currentUser.setLocation(currentLocation);
                                updateCustomerInfo();
                            }
                        } else {
                            Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error fetching location", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation();
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
