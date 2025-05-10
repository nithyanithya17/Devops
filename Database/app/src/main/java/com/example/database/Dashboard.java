package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class Dashboard extends AppCompatActivity {

    private Button btnTips, btnDonate, btnLearnMore, btnBackToHome;

    // Firebase reference
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);  // Ensure the layout is correct

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();

        // Initialize the buttons
        btnTips = findViewById(R.id.btnTips);  // Ensure the ID matches
        btnDonate = findViewById(R.id.btnDonate);  // Ensure the ID matches
        btnLearnMore = findViewById(R.id.btnLearnMore);  // Ensure the ID matches
        btnBackToHome = findViewById(R.id.btnBackToHome);  // Ensure the ID matches

        // Navigate to Water Conservation Tips Screen
        btnTips.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Reports.class);
            startActivity(intent);
        });

        // Navigate to Donation Page
        btnDonate.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Donation.class);
            startActivity(intent);
        });

        // Navigate to Learn More Page about SDG 6
        btnLearnMore.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, LearnMore.class);
            startActivity(intent);
        });

        // Navigate back to the Home Dashboard
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, DailywaterConsumption.class);
            startActivity(intent);
            finish();
        });

        // Example Firebase operation (writing some data to Firebase)
        // Just an example, ensure you modify it according to your app's needs.
        writeToFirebase("Sample Data");
    }

    // Sample method to write to Firebase
    private void writeToFirebase(String data) {
        // Reference to your Firebase node
        database.getReference("data").setValue(data);
    }
}
