package com.example.optimalstate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView newUserTextView, forgotPasswordTextView;

    // Declare FirebaseAuth instance
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main); // Ensure this is the correct layout

        // Initialize FirebaseAuth and Firestore instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        newUserTextView = findViewById(R.id.new_user);
        forgotPasswordTextView = findViewById(R.id.forgot_password);

        // Set onClickListener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Firebase login
                handleLogin();
            }
        });

        // Handle "New User" click
        newUserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Create Account screen
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class); // This is for CreateAccountActivity
                startActivity(intent);
            }
        });

        // Handle "Forgot Password" click (optional)
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Forgot Password screen if needed
                // You can implement this activity if you want to provide a password reset functionality
                // Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                // startActivity(intent);
            }
        });
    }

    // Handle login logic with Firebase Authentication
    private void handleLogin() {
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase authentication login process
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // If login is successful, get the current user
                        FirebaseUser user = mAuth.getCurrentUser();

                        // If the user is logged in, retrieve the user data from Firestore
                        if (user != null) {
                            db.collection("users").document(user.getUid())
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            // Retrieve user data (isProvider and fullName)
                                            UserData userData = documentSnapshot.toObject(UserData.class);
                                            if (userData != null) {
                                                // Check if the user is a provider
                                                if (userData.isProvider()) {
                                                    // Navigate to ProviderHomeActivity
                                                    Intent providerIntent = new Intent(MainActivity.this, ProviderHomeActivity.class);
                                                    startActivity(providerIntent);
                                                    finish();
                                                } else {
                                                    // Navigate to UserHomeActivity
                                                    Intent userIntent = new Intent(MainActivity.this, UserHomeActivity.class);
                                                    startActivity(userIntent);
                                                    finish();
                                                }
                                            }
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(MainActivity.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // If login fails, display an error message
                        Toast.makeText(MainActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
