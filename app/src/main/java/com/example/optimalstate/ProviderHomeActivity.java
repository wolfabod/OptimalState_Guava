package com.example.optimalstate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ProviderHomeActivity extends AppCompatActivity {

    private Button addClientButton, removeClientButton, viewClientsButton, settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home); // Ensure this is the correct layout

        // Initialize buttons
        addClientButton = findViewById(R.id.addClientButton);
        removeClientButton = findViewById(R.id.removeClientButton);
        viewClientsButton = findViewById(R.id.viewClientsButton);
        settingsButton = findViewById(R.id.settingsButton);

        // Set onClickListeners for buttons
        addClientButton.setOnClickListener(v -> {
            // Navigate to AddClientActivity (to be implemented later)
            Intent addClientIntent = new Intent(ProviderHomeActivity.this, ProviderAddClientActivity.class);
            startActivity(addClientIntent);
        });

        removeClientButton.setOnClickListener(v -> {
            // Navigate to RemoveClientActivity (to be implemented later)
            Intent removeClientIntent = new Intent(ProviderHomeActivity.this, ProviderRemoveClientActivity.class);
            startActivity(removeClientIntent);
        });

        viewClientsButton.setOnClickListener(v -> {
            // Navigate to ViewClientsActivity (to be implemented later)
            Intent viewClientsIntent = new Intent(ProviderHomeActivity.this, ProviderViewClientsActivity.class);
            startActivity(viewClientsIntent);
        });

        settingsButton.setOnClickListener(v -> {
            // Navigate to ProviderSettingsActivity (or any settings activity for providers)
            Intent settingsIntent = new Intent(ProviderHomeActivity.this, ProviderSettingsActivity.class);
            startActivity(settingsIntent);
        });
    }
}
