package com.example.app.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etCCCD, etPhone, etEmail;
    private MaterialButton btnContinue, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        etName = findViewById(R.id.et_reg_name);
        etCCCD = findViewById(R.id.et_reg_cccd);
        etPhone = findViewById(R.id.et_reg_phone);
        etEmail = findViewById(R.id.et_reg_email);

        btnContinue = findViewById(R.id.btn_continue);
        btnCancel = findViewById(R.id.btn_cancel);

        setupListeners();
    }

    private void setupListeners() {
        btnContinue.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String cccd = etCCCD.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (name.isEmpty() || cccd.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(RegisterActivity.this, RegisterFaceAuthActivity.class);
            intent.putExtra("USER_NAME", name);
            intent.putExtra("USER_PHONE", phone);

            startActivity(intent);
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}