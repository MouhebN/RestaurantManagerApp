package tn.esprit.restaurantmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import tn.esprit.restaurantmanagerapp.entity.Customer;
import tn.esprit.restaurantmanagerapp.repository.CustomerRepository;
import tn.esprit.restaurantmanagerapp.utils.EmailSender;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signupButton;
    private TextView resetPasswordTextView, goToLoginTextView;
    private CustomerRepository customerRepository;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        initializeViews();

        // Initialize repository and executor service
        customerRepository = new CustomerRepository(this);
        executorService = Executors.newSingleThreadExecutor();

        // Set listeners
        setupListeners();
    }

    private void initializeViews() {
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        signupButton = findViewById(R.id.signup_button);
        resetPasswordTextView = findViewById(R.id.reset_password);
        goToLoginTextView = findViewById(R.id.go_to_login);
    }

    private void setupListeners() {
        // Listener for the "Sign Up" button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Listener for "Forgot Password" text view
        resetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        // Listener for "Login" text view
        goToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, SigninActivity.class));
            }
        });
    }

    private void registerUser() {
        try {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (!validateInput(username, email, password, confirmPassword)) return;

            String hashedPassword = hashPassword(password);

            executorService.execute(() -> {
                try {
                    boolean emailExists = customerRepository.checkEmailExists(email) > 0;
                    runOnUiThread(() -> {
                        if (emailExists) {
                            Toast.makeText(SignupActivity.this, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
                        } else {
                            createNewCustomer(username, email, hashedPassword);
                        }
                    });
                } catch (Exception e) {
                    Log.e("SignupActivity", "Error checking email existence: ", e);
                    runOnUiThread(() -> Toast.makeText(SignupActivity.this, "Error occurred during signup.", Toast.LENGTH_SHORT).show());
                }
            });
        } catch (Exception e) {
            Log.e("SignupActivity", "Error in registerUser: ", e);
            Toast.makeText(SignupActivity.this, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String username, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createNewCustomer(String username, String email, String hashedPassword) {
        Log.d("SignupActivity", "Registering user: " + username + ", " + email);

        // Proceed with registration if email is unique
        Customer newCustomer = new Customer(username, email, hashedPassword);
        executorService.execute(() -> {
            customerRepository.insertCustomer(newCustomer);
            runOnUiThread(() -> {
                Toast.makeText(SignupActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                // Send welcome email
                EmailSender emailSender = new EmailSender();
                emailSender.sendWelcomeEmail(email, username);

                // Redirect to DashboardActivity
                Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
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
