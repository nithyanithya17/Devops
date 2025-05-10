package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText email, password;
    private Button btnLogin, btnRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Handle login button click
        btnLogin.setOnClickListener(v -> {
            String emailTxt = email.getText().toString().trim();
            String passwordTxt = password.getText().toString().trim();

            if (TextUtils.isEmpty(emailTxt) || TextUtils.isEmpty(passwordTxt)) {
                Toast.makeText(this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(emailTxt, passwordTxt)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Login.this, Dashboard.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Handle register button click
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class); // Assuming you have a Register activity
            startActivity(intent);
        });
    }
}
