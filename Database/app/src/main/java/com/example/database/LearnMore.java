package com.example.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LearnMore extends Activity {

    private Button buttonBack;

    // Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize the AboutUs button
        buttonBack = findViewById(R.id.buttonBack);

        // Navigate to AboutUs activity
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(LearnMore.this, AboutUs.class);
            startActivity(intent);
        });
    }
}
