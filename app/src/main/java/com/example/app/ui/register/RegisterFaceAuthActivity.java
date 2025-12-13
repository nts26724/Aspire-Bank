package com.example.app.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.data.model.User;
import com.example.app.data.repository.UserRepository;
import com.example.app.interfaces.UserCallback;
import com.google.android.material.button.MaterialButton;

public class RegisterFaceAuthActivity extends AppCompatActivity {

    private MaterialButton btnScan, btnCancel;
    private UserRepository userRepository;
    private String fullName, cccd, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_face_auth);

        userRepository = new UserRepository();

        Intent intent = getIntent();
        if (intent != null) {
            fullName = intent.getStringExtra("FULL_NAME");
            cccd = intent.getStringExtra("CCCD");
            phone = intent.getStringExtra("PHONE");
            email = intent.getStringExtra("EMAIL");
        }

        btnScan = findViewById(R.id.btn_start_scan);
        btnCancel = findViewById(R.id.btn_cancel);

        btnScan.setOnClickListener(v -> {
            Toast.makeText(this, "Đang quét khuôn mặt...", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> {
                User newUser = new User(phone, fullName, cccd, email);

                userRepository.registerUser(newUser, new UserCallback() {
                    @Override
                    public void onSuccess() {
                        Intent nextIntent = new Intent(RegisterFaceAuthActivity.this, RegisterSuccessActivity.class);
                        startActivity(nextIntent);
                        finish();
                    }

                    @Override
                    public void onUserLoaded(User user) {
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(RegisterFaceAuthActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }, 2000);
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}