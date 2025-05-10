package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RainwaterHarvesting extends AppCompatActivity {

    Button btnBack; // This will navigate to Settings activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainwater_harvesting); // Make sure your XML file is named correctly

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(RainwaterHarvesting.this, Settings.class); // Navigate to Settings.java
            startActivity(intent);
            finish(); // Optional: Closes current activity
        });
    }
}
