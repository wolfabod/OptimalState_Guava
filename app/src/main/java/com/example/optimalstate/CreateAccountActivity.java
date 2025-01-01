package com.example.optimalstate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText fullNameEditText, emailEditText, passwordEditText;
    private Spinner daySpinner, monthSpinner, yearSpinner;
    private CheckBox isProviderCheckBox;
    private Button registerButton;
    private TextView backToLoginLink;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account); // Your provided XML file

        // Initialize FirebaseAuth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        fullNameEditText = findViewById(R.id.full_name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        daySpinner = findViewById(R.id.day_spinner);
        monthSpinner = findViewById(R.id.month_spinner);
        yearSpinner = findViewById(R.id.year_spinner);
        isProviderCheckBox = findViewById(R.id.is_provider_checkbox);
        registerButton = findViewById(R.id.register_button);
        backToLoginLink = findViewById(R.id.back_to_login_link);

        // Populate Spinners
        populateSpinners();

        // Handle register button click
        registerButton.setOnClickListener(v -> createAccount());

        // Handle back to login link click
        backToLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class); // Go back to login screen
            startActivity(intent);
            finish();
        });
    }

    private void populateSpinners() {
        // Day Spinner (1-31)
        List<String> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(String.format("%02d", i)); // Adds "01", "02", ..., "31"
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        // Month Spinner (1-12)
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(String.format("%02d", i)); // Adds "01", "02", ..., "12"
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // Year Spinner (current year to 100 years ago)
        List<String> years = new ArrayList<>();
        int currentYear = 2024; // You can use Calendar.getInstance().get(Calendar.YEAR) to get the current year
        for (int i = currentYear; i >= currentYear - 100; i--) {
            years.add(String.valueOf(i)); // Adds years from current year back 100 years
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
    }

    // Method to handle account creation
    private void createAccount() {
        // Retrieve the values entered by the user
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String day = daySpinner.getSelectedItem().toString();
        String month = monthSpinner.getSelectedItem().toString();
        String year = yearSpinner.getSelectedItem().toString();
        boolean isProvider = isProviderCheckBox.isChecked();

        // Validate user inputs
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || day.equals("DD") || month.equals("MM") || year.equals("YY")) {
            Toast.makeText(CreateAccountActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user account using Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // If sign-up is successful, store additional data in Firestore
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Prepare data to store in Firestore
                            storeUserData(user.getUid(), fullName, email, isProvider);

                            Toast.makeText(CreateAccountActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                            // Proceed to the login or home screen
                            startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        // If sign-up fails, display an error message
                        Toast.makeText(CreateAccountActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Method to store user data in Firestore
    private void storeUserData(String userId, String fullName, String email, boolean isProvider) {
        // Prepare user data for Firestore
        UserData userData = new UserData(fullName, email, isProvider);

        // Save the user data in Firestore under the "users" collection
        db.collection("users")
                .document(userId) // Use Firebase UID as the document ID
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    // Data saved successfully
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateAccountActivity.this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
