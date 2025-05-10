package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnStart, btnLogout;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Ensure this matches your XML filename

        auth = FirebaseAuth.getInstance();

        tvWelcome = findViewById(R.id.tvWelcome);
        btnStart = findViewById(R.id.btnStart);
        btnLogout = findViewById(R.id.btnLogout);

        // Navigate to another activity when "Get Started" button is clicked
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Dashboard.class);
            startActivity(intent);
        });

        // Logout and return to Login screen
        btnLogout.setOnClickListener(v -> {
            auth.signOut(); // Firebase logout
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            finish(); // Prevent going back to HomeActivity
        });
    }
}
