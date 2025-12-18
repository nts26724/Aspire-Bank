package com.example.app.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.google.android.material.button.MaterialButton;

public class RegisterFaceAuthActivity extends AppCompatActivity {

    private MaterialButton btnScan, btnCancel;
    private String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_face_auth);

        Intent intent = getIntent();
        if (intent != null) {
            fullName = intent.getStringExtra("FULL_NAME");
        }

        btnScan = findViewById(R.id.btn_start_scan);
        btnCancel = findViewById(R.id.btn_cancel);

        btnScan.setOnClickListener(v -> {
            Toast.makeText(this, "Đang quét khuôn mặt...", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> {
                Intent nextIntent = new Intent(RegisterFaceAuthActivity.this, RegisterSuccessActivity.class);
                startActivity(nextIntent);
                finish();
            }, 2000);
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}