package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Reports extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;

    TextView tvDonation, tvWaterUsed;
    EditText etDonationInput, etWaterUsedInput;
    Button btnSubmitData, btnMoreReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Check login and email verification status
        if (currentUser == null || !currentUser.isEmailVerified()) {
            Toast.makeText(this, "Please verify your email and log in to access reports", Toast.LENGTH_LONG).show();

            // Optionally, send a verification email
            if (currentUser != null && !currentUser.isEmailVerified()) {
                currentUser.sendEmailVerification()
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Verification email sent!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to send verification email: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            // Redirect to login screen
            startActivity(new Intent(Reports.this, Login.class));
            finish();
            return;
        }

        // Initialize UI elements
        tvDonation = findViewById(R.id.tvDonation);
        tvWaterUsed = findViewById(R.id.tvWaterUsed);
        etDonationInput = findViewById(R.id.etDonationInput);
        etWaterUsedInput = findViewById(R.id.etWaterUsedInput);
        btnSubmitData = findViewById(R.id.btnSubmitData);
        btnMoreReports = findViewById(R.id.btnMoreReports);

        // Fetch user-specific data
        fetchDataFromFirestore();

        // Submit new data
        btnSubmitData.setOnClickListener(view -> submitDataToFirestore());

        // Navigate to Rainwater Harvesting guide
        btnMoreReports.setOnClickListener(view -> {
            Intent intent = new Intent(Reports.this, RainwaterHarvesting.class);
            startActivity(intent);
        });
    }

    private void fetchDataFromFirestore() {
        String userId = currentUser.getUid();
        DocumentReference docRef = db.collection("stats").document(userId);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    String totalDonations = doc.getString("totalDonations");
                    String waterUsed = doc.getString("waterUsed");

                    tvDonation.setText("LKR " + (totalDonations != null ? totalDonations : "0.00"));
                    tvWaterUsed.setText((waterUsed != null ? waterUsed : "0") + " Liters");
                } else {
                    tvDonation.setText("LKR 0.00");
                    tvWaterUsed.setText("0 Liters");
                }
            } else {
                Log.w("Firestore", "Fetch failed", task.getException());
            }
        });
    }

    private void submitDataToFirestore() {
        String newDonation = etDonationInput.getText().toString().trim();
        String newWaterUsed = etWaterUsedInput.getText().toString().trim();

        if (newDonation.isEmpty() || newWaterUsed.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        DocumentReference docRef = db.collection("stats").document(userId);

        docRef.set(new StatEntry(newDonation, newWaterUsed))
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Data updated successfully!", Toast.LENGTH_SHORT).show();
                    fetchDataFromFirestore();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    public static class StatEntry {
        public String totalDonations;
        public String waterUsed;

        public StatEntry() {}

        public StatEntry(String totalDonations, String waterUsed) {
            this.totalDonations = totalDonations;
            this.waterUsed = waterUsed;
        }
    }
}
