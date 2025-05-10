package com.example.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends Activity {

    private EditText nameEditText, emailEditText, phoneEditText, addressEditText;
    private Button saveButton, logoutButton, deleteAccountButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.this, Login.class));
            finish();
            return;
        }

        userId = currentUser.getUid();

        // Initialize UI components
        nameEditText = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.editTextEmail);
        phoneEditText = findViewById(R.id.editTextPhone);
        addressEditText = findViewById(R.id.editTextAddress);
        saveButton = findViewById(R.id.buttonSave);
        logoutButton = findViewById(R.id.buttonLogout);
        deleteAccountButton = findViewById(R.id.buttonDelete);

        // Load user data from Firestore
        loadUserData();

        // Save user data to Firestore
        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();

            if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !address.isEmpty()) {
                saveUserData(name, email, phone, address);
            } else {
                Toast.makeText(Settings.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Logout from Firebase
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(Settings.this, "Logged Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.this, Login.class));
            finish();
        });

        // Delete account from Firebase
        deleteAccountButton.setOnClickListener(v -> deleteAccount());
    }

    private void loadUserData() {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String phone = documentSnapshot.getString("phone");
                String address = documentSnapshot.getString("address");

                nameEditText.setText(name != null ? name : "");
                emailEditText.setText(email != null ? email : "");
                phoneEditText.setText(phone != null ? phone : "");
                addressEditText.setText(address != null ? address : "");
            } else {
                Toast.makeText(Settings.this, "No user data found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(Settings.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveUserData(String name, String email, String phone, String address) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.update("name", name, "email", email, "phone", phone, "address", address)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Settings.this, "Changes Saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Settings.this, "Failed to save changes", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteAccount() {
        // First, delete the user data from Firestore
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.delete().addOnSuccessListener(aVoid -> {
            // Then, delete the user's account from Firebase Authentication
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                user.delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Settings.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                        // Redirect to login screen after account deletion
                        startActivity(new Intent(Settings.this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(Settings.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(Settings.this, "Failed to delete user data", Toast.LENGTH_SHORT).show();
        });
    }
}
