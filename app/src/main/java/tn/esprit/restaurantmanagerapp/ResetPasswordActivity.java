package tn.esprit.restaurantmanagerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        // Initialize views
        emailEditText = findViewById(R.id.reset_email);
        resetPasswordButton = findViewById(R.id.reset_password_button);

        // Set click listener for the reset password button
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the reset password logic (e.g., send email)
                    // You can implement the actual password reset email logic here
                    Toast.makeText(ResetPasswordActivity.this, "Password reset link sent to " + email, Toast.LENGTH_SHORT).show();

                    // Optionally, finish the activity and go back to the login screen
                    finish();
                }
            }
        });
    }
}
