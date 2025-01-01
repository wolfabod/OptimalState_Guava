package com.example.optimalstate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserAssessmentActivity extends AppCompatActivity {

    private Button whiteRegionButton, redRegionButton, yellowRegionButton, blueRegionButton, submitButton, backButton;
    private List<String> selectedFeelings;  // List to store selected feelings
    private FirebaseFirestore db;  // Firestore instance
    private FirebaseAuth mAuth; // FirebaseAuth instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_assessment); // Your provided XML layout

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize buttons and selectedFeelings list
        whiteRegionButton = findViewById(R.id.whiteregion);
        redRegionButton = findViewById(R.id.redregion);
        yellowRegionButton = findViewById(R.id.yellowregion);
        blueRegionButton = findViewById(R.id.blueregion);
        submitButton = findViewById(R.id.btn_submit);
        backButton = findViewById(R.id.btn_back);

        selectedFeelings = new ArrayList<>();  // Initialize list for feelings

        // Set onClickListeners for region buttons
        whiteRegionButton.setOnClickListener(v -> toggleFeeling("White"));
        redRegionButton.setOnClickListener(v -> toggleFeeling("Red"));
        yellowRegionButton.setOnClickListener(v -> toggleFeeling("Yellow"));
        blueRegionButton.setOnClickListener(v -> toggleFeeling("Blue"));

        // Set onClickListener for submit button
        submitButton.setOnClickListener(v -> submitAssessment());

        // Set onClickListener for back button
        backButton.setOnClickListener(v -> finish());
    }

    // Toggle feelings when a region is clicked
    private void toggleFeeling(String feeling) {
        if (selectedFeelings.contains(feeling)) {
            selectedFeelings.remove(feeling); // Remove feeling if already selected
        } else {
            selectedFeelings.add(feeling); // Add feeling if not selected
        }
    }

    // Submit the assessment and save it to Firestore
    private void submitAssessment() {
        if (selectedFeelings.isEmpty()) {
            Toast.makeText(this, "Please select at least one feeling", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current date and time
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());

        // Create Timestamp for Firestore date field
        Timestamp timestamp = Timestamp.now();  // Get current timestamp

        // Log the selected feelings and timestamp for debugging
        StringBuilder feelingsMessage = new StringBuilder("Selected feelings: ");
        for (String feeling : selectedFeelings) {
            feelingsMessage.append(feeling).append(", ");
        }
        Toast.makeText(this, feelingsMessage.toString(), Toast.LENGTH_LONG).show();

        // Get the current userId from Firebase Authentication
        String userId = mAuth.getCurrentUser().getUid();

        // Now, query Firestore to get the user's fullName (which will be used as the userId)
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String fullName = document.getString("fullName");

                            // Save the data to Firestore
                            saveAssessmentToFirestore(timestamp, currentTime, fullName, userId);
                        } else {
                            Toast.makeText(UserAssessmentActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UserAssessmentActivity.this, "Error getting user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Save the assessment to Firestore
    private void saveAssessmentToFirestore(Timestamp timestamp, String currentTime, String fullName, String userId) {
        // Create a new AssessmentData object
        AssessmentData assessmentData = new AssessmentData(timestamp, currentTime, selectedFeelings, fullName, userId);

        // Save data to Firestore
        db.collection("assessments")
                .add(assessmentData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(UserAssessmentActivity.this, "Assessment saved successfully!", Toast.LENGTH_SHORT).show();

                    // Navigate to the Exercise Activity
                    Intent intent = new Intent(UserAssessmentActivity.this, UserExerciseActivity.class);
                    intent.putExtra("selectedFeelings", selectedFeelings.toArray(new String[0])); // Pass feelings to next activity
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserAssessmentActivity.this, "Error saving assessment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
