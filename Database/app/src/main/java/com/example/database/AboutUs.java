package com.example.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AboutUs extends Activity {

    // Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Button for navigation (optional, if you add one in XML)
    private Button backToMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Optional: If you want a button to go back to MainActivity
        backToMainButton = findViewById(R.id.buttonBackToMain);

        if (backToMainButton != null) {
            backToMainButton.setOnClickListener(v -> {
                Intent intent = new Intent(AboutUs.this, OfflineTips.class);
                startActivity(intent);
                finish();
            });
        }
    }
}
