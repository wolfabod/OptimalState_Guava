package com.example.optimalstate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserHomeActivity extends AppCompatActivity {

    // Declare buttons for the UserHomeActivity layout
    private Button takeAssessmentButton, viewExercisesButton, viewHistoryButton, settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home); // Make sure the XML is named correctly

        // Initialize buttons
        takeAssessmentButton = findViewById(R.id.take_assessment_button);
        viewExercisesButton = findViewById(R.id.view_exercises_button);
        viewHistoryButton = findViewById(R.id.view_history_button);
        settingsButton = findViewById(R.id.settings_button);

        // Handle the Take Assessment button click
        takeAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the UserAssessmentActivity
                Intent intent = new Intent(UserHomeActivity.this, UserAssessmentActivity.class);
                startActivity(intent);
            }
        });

        // Handle the View Exercises button click
        viewExercisesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the UserExerciseActivity
                Intent intent = new Intent(UserHomeActivity.this, UserExerciseActivity.class);

                // Check if the user has selected any feelings previously
                // If not, we can send an empty Set or some default value
                Set<String> selectedFeelings = new HashSet<>(); // Empty set if no feelings were selected
                intent.putExtra("selectedFeelings", (Serializable) selectedFeelings);

                startActivity(intent);
            }
        });

        // Handle the View History button click
        viewHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the ViewHistoryActivity (implement this activity if needed)
                Intent intent = new Intent(UserHomeActivity.this, UserHistoryActivity.class);
                startActivity(intent);
            }
        });

        // Handle the Settings button click
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the SettingsActivity (implement this activity if needed)
                Intent intent = new Intent(UserHomeActivity.this, UserSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
