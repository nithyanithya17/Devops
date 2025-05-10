package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

public class OfflineTips extends AppCompatActivity {

    private Button buttonBackToMainOffline;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_tips);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Log a Firebase event (optional)
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "OfflineTipsActivity");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "OfflineTipsActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        // Initialize UI components
        buttonBackToMainOffline = findViewById(R.id.buttonBackToMainOffline);

        // Handle navigation to MainActivity (Home)
        buttonBackToMainOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OfflineTips.this, "Going back to Home", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OfflineTips.this, MainActivity.class);
                startActivity(intent);
                finish(); // optional: close this activity
            }
        });
    }
}

