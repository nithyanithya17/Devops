package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DailywaterConsumption extends AppCompatActivity {

    private EditText editTextDate, editTextWaterAmount;
    private Button buttonSave, buttonHome;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference waterDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔐 Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // 🔒 Check login
        if (currentUser == null) {
            // User not logged in, redirect to LoginActivity
            startActivity(new Intent(DailywaterConsumption.this, Login.class));
            finish(); // Prevent coming back with back button
            return;
        }

        setContentView(R.layout.activity_dailywater_consumption);

        // 👤 Get current user's UID
        String userId = currentUser.getUid();

        // 🔗 Firebase reference for this user's data
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        waterDataRef = database.getReference("DailyWaterConsumption").child(userId);

        // UI elements
        editTextDate = findViewById(R.id.editTextDate);
        editTextWaterAmount = findViewById(R.id.editTextWaterAmount);
        buttonSave = findViewById(R.id.buttonSaveWaterData);
        buttonHome = findViewById(R.id.buttonBackToHomeFromDaily);

        // Save button logic
        buttonSave.setOnClickListener(v -> saveWaterData());

        // Go home
        buttonHome.setOnClickListener(v -> {
            Intent intent = new Intent(DailywaterConsumption.this, Dashboard.class);
            startActivity(intent);
            finish();
        });
    }

    private void saveWaterData() {
        String date = editTextDate.getText().toString().trim();
        String amountStr = editTextWaterAmount.getText().toString().trim();

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(amountStr)) {
            Toast.makeText(this, "Please enter both date and amount!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            String entryId = waterDataRef.push().getKey();
            WaterEntry entry = new WaterEntry(date, amount);

            if (entryId != null) {
                waterDataRef.child(entryId).setValue(entry)
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to save data.", Toast.LENGTH_SHORT).show());
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format!", Toast.LENGTH_SHORT).show();
        }
    }

    // Model class
    public static class WaterEntry {
        public String date;
        public double amount;

        public WaterEntry() {} // Needed for Firebase

        public WaterEntry(String date, double amount) {
            this.date = date;
            this.amount = amount;
        }
    }
}
