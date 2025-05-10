package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Donation extends AppCompatActivity {

    private EditText etDonationAmount;
    private Button btnDonateNow, btnBackToDashboard, btnWaterConservationTips;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        // Initialize Firebase Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize UI components
        etDonationAmount = findViewById(R.id.etDonationAmount);
        btnDonateNow = findViewById(R.id.btnDonateNow);
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);
        btnWaterConservationTips = findViewById(R.id.btnWaterConservationTips); // Button for navigation

        // Check if the user is logged in
        if (auth.getCurrentUser() == null) {
            // If not logged in, navigate to LoginActivity
            Intent loginIntent = new Intent(Donation.this, Login.class);
            startActivity(loginIntent);
            finish(); // Close the current activity
        }

        // Handle Donate Now button click
        btnDonateNow.setOnClickListener(v -> {
            String donationAmount = etDonationAmount.getText().toString().trim();

            // Validate donation amount
            if (TextUtils.isEmpty(donationAmount)) {
                Toast.makeText(Donation.this, "Please enter a donation amount", Toast.LENGTH_SHORT).show();
                return;
            }

            // Proceed with donation process
            processDonation(donationAmount);
        });

        // Navigate back to the Dashboard
        btnBackToDashboard.setOnClickListener(v -> {
            onBackPressed();
        });

        // Navigate to Water Conservation Tips screen
        btnWaterConservationTips.setOnClickListener(v -> {
            startActivity(new Intent(Donation.this, Reports.class));
        });
    }

    private void processDonation(String amount) {
        // Get the current user's UID (user authentication required)
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "guest_user";

        // Create a DonationData object (this represents the data you want to store in Firestore)
        DonationData donation = new DonationData(amount, userId);

        // Save donation to Firebase Firestore
        db.collection("donations") // "donations" is the collection name
                .add(donation) // Add the donation data as a new document
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Donation.this, "Donation of $" + amount + " successfully processed. Thank you!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Donation.this, "Error processing donation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // DonationData class to represent the donation data model in Firestore
    public static class DonationData {
        private String amount;
        private String userId;

        // Constructor
        public DonationData(String amount, String userId) {
            this.amount = amount;
            this.userId = userId;
        }

        // Getters
        public String getAmount() {
            return amount;
        }

        public String getUserId() {
            return userId;
        }

        // Setters (optional)
        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
