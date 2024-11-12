package tn.esprit.restaurantmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import tn.esprit.restaurantmanagerapp.entity.Customer;
import tn.esprit.restaurantmanagerapp.repository.CustomerRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SigninActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signinButton;
    private CustomerRepository customerRepository;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Initialize views
        emailEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_password);
        signinButton = findViewById(R.id.login_button);

        // Initialize repository and executor service
        customerRepository = new CustomerRepository(this);
        executorService = Executors.newSingleThreadExecutor();

        // Set listener for the "Sign In" button
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });
    }

    private void signInUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        // Validate input
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hash the input password for comparison
        String hashedPassword = hashPassword(password);

        // Check if user exists in the database with the correct password
        executorService.execute(() -> {
            Customer customer = customerRepository.getCustomerByEmail(email);
            runOnUiThread(() -> {
                if (customer != null && customer.getPassword().equals(hashedPassword)) {
                    // Successful login
                    Toast.makeText(SigninActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Log.d("SigninActivity", "User logged in: " + email);

                    // Redirect to DashboardActivity
                    Intent intent = new Intent(SigninActivity.this, DashboardActivity.class);
                    intent.putExtra("username", customer.getName());
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    // Failed login
                    Toast.makeText(SigninActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
