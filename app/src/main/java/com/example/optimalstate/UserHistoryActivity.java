package com.example.optimalstate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserHistoryActivity extends AppCompatActivity {
    private Button backButton;
    private Spinner daySpinner, monthSpinner, yearSpinner;
    private TableLayout historyTable;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    private List<String> days, months, years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_history); // Your provided XML layout

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        backButton = findViewById(R.id.backButton);
        // Initialize UI components
        daySpinner = findViewById(R.id.daySpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        historyTable = findViewById(R.id.history_table);

        // Initialize Lists for Spinner items
        days = new ArrayList<>();
        months = new ArrayList<>();
        years = new ArrayList<>();

        // Populate Spinners with Day, Month, and Year values
        populateSpinners();

        // Fetch history when the date is selected
        setUpSpinners();
        backButton.setOnClickListener(v -> finish());
    }

    // Method to populate the spinners with days, months, and years
    private void populateSpinners() {
        for (int i = 1; i <= 31; i++) {
            days.add(String.format("%02d", i));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        for (int i = 1; i <= 12; i++) {
            months.add(String.format("%02d", i));
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 100; i--) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
    }

    // Set up the spinners to listen for date selection
    private void setUpSpinners() {
        AdapterView.OnItemSelectedListener dateListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fetchHistory(); // Fetch the history when a date is selected
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        };

        // Set listeners for all the spinners
        daySpinner.setOnItemSelectedListener(dateListener);
        monthSpinner.setOnItemSelectedListener(dateListener);
        yearSpinner.setOnItemSelectedListener(dateListener);
    }

    // Fetch history data from Firestore
    private void fetchHistory() {
        String selectedDay = daySpinner.getSelectedItem().toString();
        String selectedMonth = monthSpinner.getSelectedItem().toString();
        String selectedYear = yearSpinner.getSelectedItem().toString();

        // Validate the selected date
        if (selectedDay.equals("DD") || selectedMonth.equals("MM") || selectedYear.equals("YY")) {
            return; // Do nothing if the date is incomplete
        }

        // Create the date string
        String selectedDate = selectedYear + "-" + selectedMonth + "-" + selectedDay;

        // Ensure the user is logged in
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current userId
        String userId = mAuth.getCurrentUser().getUid();

        // Query Firestore for the assessments based on the selected date and userId
        db.collection("assessments")
                .whereEqualTo("userId", userId) // Filter by userId
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        historyTable.removeAllViews(); // Clear any previous data

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Timestamp date = document.getTimestamp("date");
                            if (date != null && isSameDate(date, selectedDate)) {
                                String time = document.getString("time");
                                List<String> feelings = (List<String>) document.get("feelings");

                                // Handle null feelings
                                if (feelings == null) {
                                    feelings = new ArrayList<>();
                                }

                                // Create a new row for each assessment entry
                                TableRow row = new TableRow(this);
                                TextView timeTextView = new TextView(this);
                                timeTextView.setText(time);
                                TextView feelingsTextView = new TextView(this);
                                feelingsTextView.setText(String.join(", ", feelings));

                                // Add the time and feelings to the row
                                row.addView(timeTextView);
                                row.addView(feelingsTextView);

                                // Add the row to the table
                                historyTable.addView(row);
                            }
                        }
                    } else {
                        Toast.makeText(UserHistoryActivity.this, "Failed to fetch history: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Check if the timestamp matches the selected date
    private boolean isSameDate(Timestamp timestamp, String selectedDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String timestampDate = sdf.format(timestamp.toDate());
        return timestampDate.equals(selectedDate);
    }
}
