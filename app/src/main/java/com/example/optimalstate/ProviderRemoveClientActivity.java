package com.example.optimalstate;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProviderRemoveClientActivity extends AppCompatActivity {

    private ListView lvClientList;
    private Button btnConfirmRemove, btnBackRemoveClient;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private List<String> clientNames;
    private List<String> clientIds;
    private ArrayAdapter<String> adapter;
    private int selectedPosition = -1;  // To track the selected client

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_remove_client); // Ensure this is the correct layout

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        lvClientList = findViewById(R.id.lv_client_list);
        btnConfirmRemove = findViewById(R.id.btn_confirm_remove);
        btnBackRemoveClient = findViewById(R.id.btn_back_remove_client);

        // Initialize lists
        clientNames = new ArrayList<>();
        clientIds = new ArrayList<>();

        // Set up adapter for ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clientNames);
        lvClientList.setAdapter(adapter);

        // Fetch clients from Firestore
        fetchClients();

        // Handle list item clicks to select clients
        lvClientList.setOnItemClickListener((parent, view, position, id) -> {
            // Mark the selected client
            selectedPosition = position;  // Store the position of the selected client
            Toast.makeText(ProviderRemoveClientActivity.this, "Client selected: " + clientNames.get(position), Toast.LENGTH_SHORT).show();
        });

        // Handle the Confirm button click to remove the selected client
        btnConfirmRemove.setOnClickListener(v -> {
            if (selectedPosition != -1) {
                String clientId = clientIds.get(selectedPosition);
                removeClient(clientId);
            } else {
                Toast.makeText(ProviderRemoveClientActivity.this, "Please select a client to remove", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle the Back button click
        btnBackRemoveClient.setOnClickListener(v -> {
            // Navigate back to the ProviderHomeActivity
            finish();
        });
    }

    // Method to fetch all clients from Firestore
    private void fetchClients() {
        db.collection("users") // Make sure you're querying the correct collection ("users" in Firestore)
                .whereEqualTo("provider", false) // Filter for clients only (provider should be false)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        clientNames.clear();
                        clientIds.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String clientId = document.getId(); // Get the client ID (Firestore document ID)
                            String username = document.getString("fullName"); // Get the client's full name

                            // Ensure that the username and clientId are not null before adding
                            if (username != null && clientId != null) {
                                clientNames.add(username); // Add the client's full name to the list
                                clientIds.add(clientId); // Add the client's Firestore ID to the list
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notify the adapter that the data has been updated
                    } else {
                        Toast.makeText(ProviderRemoveClientActivity.this, "Failed to load clients: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to remove the selected client from Firestore and Firebase Authentication
    private void removeClient(String clientId) {
        // Remove the client from Firestore
        db.collection("users").document(clientId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // If Firestore deletion is successful, remove from Firebase Authentication
                    FirebaseAuth.getInstance().getCurrentUser().delete()
                            .addOnCompleteListener(deleteTask -> {
                                if (deleteTask.isSuccessful()) {
                                    Toast.makeText(ProviderRemoveClientActivity.this, "Client removed successfully", Toast.LENGTH_SHORT).show();
                                    // Refresh the list of clients
                                    fetchClients();
                                } else {
                                    Toast.makeText(ProviderRemoveClientActivity.this, "Failed to delete client from Firebase Authentication: " + deleteTask.getException(), Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProviderRemoveClientActivity.this, "Error removing client: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
