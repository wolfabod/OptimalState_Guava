package com.example.optimalstate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserSettingsActivity extends AppCompatActivity {

    private Button changeUserSettingsButton, setNotificationsButton, shareWithButton, logOutButton, backButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings); // Your provided XML layout

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE);

        // Initialize Buttons
        changeUserSettingsButton = findViewById(R.id.changeUserSettingsButton);
        setNotificationsButton = findViewById(R.id.setNotificationsButton);
        shareWithButton = findViewById(R.id.shareWithButton);
        logOutButton = findViewById(R.id.LogOutButton);
        backButton = findViewById(R.id.backButton);

        // Handle the Change User Settings button click
        changeUserSettingsButton.setOnClickListener(v -> changeUserSettings());

        // Handle the Set Notifications button click
        setNotificationsButton.setOnClickListener(v -> setNotifications());

        // Handle the Share With button click
        shareWithButton.setOnClickListener(v -> shareWith());

        // Handle the LogOut button click
        logOutButton.setOnClickListener(v -> logOut());

        // Handle the Back button click
        backButton.setOnClickListener(v -> goBack());
    }

    // Method to change user settings (user ID - full name)
    private void changeUserSettings() {
        EditText newUserIdEditText = new EditText(this);
        newUserIdEditText.setHint("Enter new user ID");

        // Show a prompt for the user to input new ID
        new android.app.AlertDialog.Builder(this)
                .setTitle("Change User ID")
                .setView(newUserIdEditText)
                .setPositiveButton("Change", (dialog, which) -> {
                    String newUserId = newUserIdEditText.getText().toString().trim();
                    if (!newUserId.isEmpty()) {
                        updateUserIdInFirebase(newUserId);
                    } else {
                        Toast.makeText(this, "Please enter a new user ID", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Update the user's ID in Firestore
    private void updateUserIdInFirebase(String newUserId) {
        String currentUserId = mAuth.getCurrentUser().getUid();
        db.collection("users")
                .document(currentUserId)
                .update("fullName", newUserId)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "User ID updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update user ID", Toast.LENGTH_SHORT).show();
                });
    }

    // Set the notification for check-in at a specific date and time
    private void setNotifications() {
        // Create EditText fields to prompt the user for the date and time
        EditText dateInput = new EditText(this);
        dateInput.setHint("Enter Date (yyyy-MM-dd)");

        EditText timeInput = new EditText(this);
        timeInput.setHint("Enter Time (HH:mm)");

        // Show a dialog for the user to enter the date and time
        new android.app.AlertDialog.Builder(this)
                .setTitle("Set Notification Time")
                .setMessage("Enter the date and time for your notification")
                .setView(dateInput)
                .setPositiveButton("Set", (dialog, which) -> {
                    String date = dateInput.getText().toString().trim();
                    String time = timeInput.getText().toString().trim();

                    if (!date.isEmpty() && !time.isEmpty()) {
                        setAlarm(date, time); // Set the alarm with the user-provided date and time
                    } else {
                        Toast.makeText(UserSettingsActivity.this, "Please enter both date and time.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Method to set the alarm based on the user input date and time
    private void setAlarm(String date, String time) {
        try {
            // Parse the date and time input by the user
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar calendar = Calendar.getInstance();

            // Combine date and time into one string and parse it
            String dateTime = date + " " + time;
            calendar.setTime(dateFormat.parse(dateTime));

            // Set the alarm to trigger at the specified date and time
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, NotificationReceiver.class); // NotificationReceiver will show the notification
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Set the alarm
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

            Toast.makeText(this, "Notification set for " + date + " " + time, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Invalid date or time format.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to copy the link to clipboard
    private void shareWith() {
        String shareableLink = "https://github.com/wolfabod/Guava_OptimalState";  // Replace with actual link
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Shared Link", shareableLink);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    // Method to log out the user
    private void logOut() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to MainActivity (Login screen)
        Intent intent = new Intent(UserSettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Navigate back to UserHomeActivity
    private void goBack() {
        Intent intent = new Intent(UserSettingsActivity.this, UserHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
