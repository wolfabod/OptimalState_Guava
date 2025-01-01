package com.example.optimalstate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderViewClientsActivity extends AppCompatActivity {

    private TableLayout clientTable;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_view_clients); // Your XML layout

        db = FirebaseFirestore.getInstance();
        clientTable = findViewById(R.id.client_table);

        // Fetch and display the clients' assessments data
        fetchClientData();

        // Back button to go back to the previous screen
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    // Fetch the assessment data for each user and display it in the table
    private void fetchClientData() {
        // Fetch all assessments from Firestore
        db.collection("assessments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, QueryDocumentSnapshot> latestAssessments = new HashMap<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userId = document.getString("userId");
                            if (userId != null) {
                                // Get the timestamp and compare to store the latest assessment
                                if (!latestAssessments.containsKey(userId) ||
                                        document.getTimestamp("date").compareTo(latestAssessments.get(userId).getTimestamp("date")) > 0) {
                                    latestAssessments.put(userId, document);
                                }
                            }
                        }

                        // Populate the table with the latest assessments data for each client
                        for (Map.Entry<String, QueryDocumentSnapshot> entry : latestAssessments.entrySet()) {
                            String userId = entry.getKey();
                            QueryDocumentSnapshot document = entry.getValue();

                            String fullName = document.getString("fullName");
                            String time = document.getString("time");
                            List<String> feelings = (List<String>) document.get("feelings");

                            // Create a row for this user
                            TableRow row = new TableRow(this);

                            // Full name column
                            TextView nameTextView = new TextView(this);
                            nameTextView.setText(fullName);
                            nameTextView.setPadding(8, 8, 8, 8);
                            row.addView(nameTextView);

                            // Last update column (Formatted Date)
                            TextView lastUpdateTextView = new TextView(this);
                            String lastUpdate = formatDate(document);
                            lastUpdateTextView.setText(lastUpdate);
                            lastUpdateTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            lastUpdateTextView.setPadding(8, 8, 8, 8);
                            row.addView(lastUpdateTextView);

                            // Time column
                            TextView timeTextView = new TextView(this);
                            timeTextView.setText(time);
                            timeTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            timeTextView.setPadding(8, 8, 8, 8);
                            row.addView(timeTextView);

                            // Status column (Feelings array)
                            TextView statusTextView = new TextView(this);
                            if (feelings != null) {
                                statusTextView.setText(String.join(", ", feelings));
                            } else {
                                statusTextView.setText("No status");
                            }
                            statusTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                            statusTextView.setPadding(8, 8, 8, 8);
                            row.addView(statusTextView);

                            // Add the row to the table
                            clientTable.addView(row);
                        }

                    } else {
                        Toast.makeText(ProviderViewClientsActivity.this, "Failed to load clients' data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to format the date into a readable string
    private String formatDate(QueryDocumentSnapshot document) {
        // Format the timestamp to only show the date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (document.getTimestamp("date") != null) {
            return sdf.format(document.getTimestamp("date").toDate());
        } else {
            return "N/A";
        }
    }
}
