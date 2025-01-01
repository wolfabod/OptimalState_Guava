package com.example.optimalstate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProviderAddClientActivity extends AppCompatActivity {

    private EditText etClientEmail, etClientUsername, etClientPassword;
    private Button btnAddClient, btnBackAddClient;

    // Declare FirebaseAuth and Firestore instances
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_add_client); // Ensure this is the correct XML layout

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        etClientEmail = findViewById(R.id.et_client_email);
        etClientUsername = findViewById(R.id.et_client_username);
        etClientPassword = findViewById(R.id.et_client_password);
        btnAddClient = findViewById(R.id.btn_add_client);
        btnBackAddClient = findViewById(R.id.btn_back_add_client);

        // Set click listener for adding client
        btnAddClient.setOnClickListener(v -> addClient());

        // Set click listener for back button
        btnBackAddClient.setOnClickListener(v -> {
            // Go back to ProviderHomeActivity
            Intent intent = new Intent(ProviderAddClientActivity.this, ProviderHomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to handle adding a client
    private void addClient() {
        String email = etClientEmail.getText().toString().trim();
        String username = etClientUsername.getText().toString().trim();
        String password = etClientPassword.getText().toString().trim();

        // Basic validation
        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(ProviderAddClientActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register the client in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // If client registration is successful, get the current user
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Create UserData to save in Firestore
                            UserData userData = new UserData(username, email, false); // `false` because the user is a client, not a provider

                            // Save the user data in Firestore under the "users" collection
                            db.collection("users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        // Show success message and navigate to the provider home screen
                                        Toast.makeText(ProviderAddClientActivity.this, "Client added successfully!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ProviderAddClientActivity.this, ProviderHomeActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ProviderAddClientActivity.this, "Error adding client: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // If registration fails, display an error message
                        Toast.makeText(ProviderAddClientActivity.this, "Error registering client: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
