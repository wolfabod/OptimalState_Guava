package com.example.optimalstate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProviderSettingsActivity extends AppCompatActivity {

    private Button changeUserSettingsButton, logOutButton, backButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_settings_provider); // This is your provided layout XML

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize Buttons
        changeUserSettingsButton = findViewById(R.id.changeUserSettingsButton);
        logOutButton = findViewById(R.id.LogoutButton);  // Logout Button
        backButton = findViewById(R.id.backButton); // Back Button

        // Handle the Change User Settings button click
        changeUserSettingsButton.setOnClickListener(v -> changeUserSettings());

        // Handle the LogOut button click
        logOutButton.setOnClickListener(v -> logOut());

        // Handle the Back button click
        backButton.setOnClickListener(v -> goBack());
    }

    // Method to change provider's username
    private void changeUserSettings() {
        // Declare the new provider name variable outside of the dialog
        final EditText newProviderNameEditText = new EditText(this);
        newProviderNameEditText.setHint("Enter new provider name");

        // Show a prompt for the provider to input new name
        new android.app.AlertDialog.Builder(this)
                .setTitle("Change Provider Name")
                .setView(newProviderNameEditText)
                .setPositiveButton("Change", (dialog, which) -> {
                    String newProviderName = newProviderNameEditText.getText().toString().trim(); // Fetch the entered name
                    if (!newProviderName.isEmpty()) {
                        updateProviderNameInFirebase(newProviderName); // Update the name in Firestore
                    } else {
                        Toast.makeText(this, "Please enter a new provider name", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Update the provider's name in Firestore
    private void updateProviderNameInFirebase(String newProviderName) {
        String currentProviderId = mAuth.getCurrentUser().getUid();
        db.collection("users")
                .document(currentProviderId)
                .update("fullName", newProviderName)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Provider name updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update provider name", Toast.LENGTH_SHORT).show();
                });
    }

    // Method to log out the provider
    private void logOut() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to MainActivity (Login screen)
        Intent intent = new Intent(ProviderSettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Navigate back to ProviderHomeActivity
    private void goBack() {
        Intent intent = new Intent(ProviderSettingsActivity.this, ProviderHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
