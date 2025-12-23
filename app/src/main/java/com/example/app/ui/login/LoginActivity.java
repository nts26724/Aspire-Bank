package com.example.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.data.model.Account;
import com.example.app.interfaces.LoginCallback;
import com.example.app.ui.homecustomer.HomeCustomerActivity;
import com.example.app.ui.homeofficer.HomeOfficerActivity;
import com.example.app.ui.register.RegisterActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText username, password;
    private Button btnLogin, btnRegister;
    private LoginViewModel loginViewModel;
    private String usernameText;
    private String passwordText;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            usernameText = username.getText().toString().trim();
            passwordText = password.getText().toString().trim();

            if (usernameText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this,
                        "Vui lòng nhập đầy đủ thông tin",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                loginViewModel.login(usernameText, passwordText);
            }

            loginViewModel.getLoginResult().observe(this, result -> {
                switch (result) {
                    case NOT_FOUND:
                        Toast.makeText(this, "Tên đăng nhập không tồn tại", Toast.LENGTH_SHORT).show();
                        break;
                    case WRONG_PASSWORD:
                        Toast.makeText(this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        Toast.makeText(this, "Không thể kết nối tới máy chủ", Toast.LENGTH_SHORT).show();
                        break;
                    case CUSTOMER:
                        Intent intentHomeCustomer = new Intent(LoginActivity.this, HomeCustomerActivity.class);
                        startActivity(intentHomeCustomer);
                        finish();
                        break;
                    case OFFICER:
                        Intent intentHomeOfficer = new Intent(LoginActivity.this, HomeOfficerActivity.class);
                        startActivity(intentHomeOfficer);
                        finish();
                        break;
                }
            });
        });
    }

    public void init() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }
}
