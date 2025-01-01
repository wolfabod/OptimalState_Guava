package com.example.optimalstate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Set;

public class UserExerciseActivity extends AppCompatActivity {

    private Button breathingExerciseButton, otherExercisesButton, recommendedFoodsButton, watchVideoButton;
    private Set<String> selectedFeelings;  // To store selected feelings from UserAssessmentActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_exercise_activity);  // The XML layout we created earlier

        // Initialize buttons
        breathingExerciseButton = findViewById(R.id.breathing_exercise_button);
        otherExercisesButton = findViewById(R.id.other_exercises_button);
        recommendedFoodsButton = findViewById(R.id.recommended_foods_button);
        watchVideoButton = findViewById(R.id.watch_video_button);

        // Get the feelings selected in UserAssessmentActivity
        Intent intent = getIntent();
        selectedFeelings = (Set<String>) intent.getSerializableExtra("selectedFeelings");

        // Set click listeners for each button
        breathingExerciseButton.setOnClickListener(v -> openExerciseVideo("Breathing"));
        otherExercisesButton.setOnClickListener(v -> openExerciseVideo("Other"));
        recommendedFoodsButton.setOnClickListener(v -> openExerciseVideo("Foods"));
        watchVideoButton.setOnClickListener(v -> openExerciseVideo("Video"));
    }

    // Method to handle the video redirection based on the selected feelings
    private void openExerciseVideo(String exerciseType) {
        String videoUrl = getVideoUrl(exerciseType);
        if (videoUrl != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(videoUrl));
            startActivity(intent);  // Open the YouTube video in the browser
        } else {
            Toast.makeText(this, "No video available for the selected exercise.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to decide which video URL to use based on selected feelings
    private String getVideoUrl(String exerciseType) {
        if (selectedFeelings.contains("Red")) {
            return getRedRegionVideoUrl(exerciseType);
        } else if (selectedFeelings.contains("White")) {
            return getWhiteRegionVideoUrl(exerciseType);
        } else if (selectedFeelings.contains("Yellow")) {
            return getYellowRegionVideoUrl(exerciseType);
        } else if (selectedFeelings.contains("Blue")) {
            return getBlueRegionVideoUrl(exerciseType);
        } else {
            return getDefaultVideoUrl(exerciseType);
        }
    }

    // Example video URLs for the Red region
    private String getRedRegionVideoUrl(String exerciseType) {
        switch (exerciseType) {
            case "Breathing":
                return "https://www.youtube.com/watch?v=exampleRedBreathing";
            case "Other":
                return "https://www.youtube.com/watch?v=exampleRedOther";
            case "Foods":
                return "https://www.youtube.com/watch?v=exampleRedFoods";
            case "Video":
                return "https://www.youtube.com/watch?v=exampleRedVideo";
            default:
                return null;
        }
    }

    // Example video URLs for the White region
    private String getWhiteRegionVideoUrl(String exerciseType) {
        switch (exerciseType) {
            case "Breathing":
                return "https://www.youtube.com/watch?v=exampleWhiteBreathing";
            case "Other":
                return "https://www.youtube.com/watch?v=exampleWhiteOther";
            case "Foods":
                return "https://www.youtube.com/watch?v=exampleWhiteFoods";
            case "Video":
                return "https://www.youtube.com/watch?v=exampleWhiteVideo";
            default:
                return null;
        }
    }

    // Example video URLs for the Yellow region
    private String getYellowRegionVideoUrl(String exerciseType) {
        switch (exerciseType) {
            case "Breathing":
                return "https://www.youtube.com/watch?v=exampleYellowBreathing";
            case "Other":
                return "https://www.youtube.com/watch?v=exampleYellowOther";
            case "Foods":
                return "https://www.youtube.com/watch?v=exampleYellowFoods";
            case "Video":
                return "https://www.youtube.com/watch?v=exampleYellowVideo";
            default:
                return null;
        }
    }

    // Example video URLs for the Blue region
    private String getBlueRegionVideoUrl(String exerciseType) {
        switch (exerciseType) {
            case "Breathing":
                return "https://www.youtube.com/watch?v=exampleBlueBreathing";
            case "Other":
                return "https://www.youtube.com/watch?v=exampleBlueOther";
            case "Foods":
                return "https://www.youtube.com/watch?v=exampleBlueFoods";
            case "Video":
                return "https://www.youtube.com/watch?v=exampleBlueVideo";
            default:
                return null;
        }
    }

    // Default video URL for when no specific region is selected
    private String getDefaultVideoUrl(String exerciseType) {
        switch (exerciseType) {
            case "Breathing":
                return "https://www.youtube.com/watch?v=defaultBreathing";
            case "Other":
                return "https://www.youtube.com/watch?v=defaultOther";
            case "Foods":
                return "https://www.youtube.com/watch?v=defaultFoods";
            case "Video":
                return "https://www.youtube.com/watch?v=defaultVideo";
            default:
                return null;
        }
    }
}
