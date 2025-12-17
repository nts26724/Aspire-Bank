package com.example.app.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etUsername, etEmail, etPhone, etBirthday, etAddress, etPassword, etRePassword;
    private RadioGroup rgGender;
    private Button btnRegister;
    private TextView tvLoginNow;

    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        initViews();
        setupListeners();
        setupObservers();
    }

    private void initViews() {
        etFullName = findViewById(R.id.et_fullname);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etBirthday = findViewById(R.id.et_birthday);
        etAddress = findViewById(R.id.et_address);
        etPassword = findViewById(R.id.et_password);
        etRePassword = findViewById(R.id.et_re_password);

        rgGender = findViewById(R.id.rg_gender);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginNow = findViewById(R.id.tv_login_now);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String rePassword = etRePassword.getText().toString().trim();
            String gender = "M";
            if (rgGender.getCheckedRadioButtonId() == R.id.rb_female) {
                gender = "F";
            }

            registerViewModel.register(
                    username,
                    password,
                    rePassword,
                    fullName,
                    phone,
                    email,
                    address,
                    birthday,
                    gender
            );
        });

        tvLoginNow.setOnClickListener(v -> finish());
    }

    private void setupObservers() {
        registerViewModel.getRegisterResult().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, RegisterFaceAuthActivity.class);
                intent.putExtra("FULL_NAME", etFullName.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });

        registerViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}