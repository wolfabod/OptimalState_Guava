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
                return "https://www.youtube.com/watch?v=DbDoBzGY3vo&ab_channel=TAKEADEEPBREATH";
            case "Other":
                return "https://www.youtube.com/watch?v=DbDoBzGY3vo&ab_channel=TAKEADEEPBREATH";
            case "Foods":
                return "https://youtu.be/JdF6kosMCqY?si=IJWsgn5P_X8iMASM";
            case "Video":
                return "https://www.youtube.com/watch?v=DbDoBzGY3vo&ab_channel=TAKEADEEPBREATH";
            default:
                return null;
        }
    }

    // Example video URLs for the White region
    private String getWhiteRegionVideoUrl(String exerciseType) {
        switch (exerciseType) {
            case "Breathing":
                return "https://www.youtube.com/watch?v=LiUnFJ8P4gM&ab_channel=Hands-OnMeditation";
            case "Other":
                return "https://www.youtube.com/watch?v=LiUnFJ8P4gM&ab_channel=Hands-OnMeditation";
            case "Foods":
                return "https://youtu.be/T7FaD6jw2vU?si=4HdlEWNYNC3Pd4qT";
            case "Video":
                return "https://www.youtube.com/watch?v=LiUnFJ8P4gM&ab_channel=Hands-OnMeditation";
            default:
                return null;
        }
    }

    // Example video URLs for the Yellow region
    private String getYellowRegionVideoUrl(String exerciseType) {
        switch (exerciseType) {
            case "Breathing":
                return "https://youtu.be/A-bm1KKOCvU?si=sdvHtXFRfZRDlJeW";
            case "Other":
                return "https://youtu.be/A-bm1KKOCvU?si=sdvHtXFRfZRDlJeW";
            case "Foods":
                return "https://youtu.be/b9D44-dgnts?si=tt9dR_uSraO-r78p";
            case "Video":
                return "https://youtu.be/A-bm1KKOCvU?si=sdvHtXFRfZRDlJeW";
            default:
                return null;
        }
    }

    // Example video URLs for the Blue region
    private String getBlueRegionVideoUrl(String exerciseType) {
        switch (exerciseType) {
            case "Breathing":
                return "https://youtu.be/8VwufJrUhic?si=9aklkge_uB_jO3yK";
            case "Other":
                return "https://youtu.be/8VwufJrUhic?si=9aklkge_uB_jO3yK";
            case "Foods":
                return "https://youtu.be/b9D44-dgnts?si=TJc3pGmlbegvAtLt";
            case "Video":
                return "https://youtu.be/8VwufJrUhic?si=9aklkge_uB_jO3yK";
            default:
                return null;
        }
    }

    // Default video URL for when no specific region is selected
    private String getDefaultVideoUrl(String exerciseType) {
        switch (exerciseType) {
            case "Breathing":
                return "https://youtu.be/N9jmO6xwFfs?si=GDaQbXu4ZT-z7cqe";
            case "Other":
                return "https://youtu.be/N9jmO6xwFfs?si=GDaQbXu4ZT-z7cqe";
            case "Foods":
                return "https://youtu.be/vFpXvhVL-10?si=WUcHKQcEbeGjudmO";
            case "Video":
                return "https://youtu.be/N9jmO6xwFfs?si=GDaQbXu4ZT-z7cqe";
            default:
                return null;
        }
    }
}
