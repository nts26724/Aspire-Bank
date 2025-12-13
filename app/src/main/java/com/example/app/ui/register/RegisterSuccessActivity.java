package com.example.app.ui.register;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import com.google.android.material.button.MaterialButton;

public class RegisterSuccessActivity extends AppCompatActivity {

    private MaterialButton btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_auth_success);

        btnBackToLogin = findViewById(R.id.btn_back_to_login);

        btnBackToLogin.setOnClickListener(v -> {
            finish();
        });
    }
}