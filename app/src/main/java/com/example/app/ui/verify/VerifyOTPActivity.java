package com.example.app.ui.verify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public abstract class VerifyOTPActivity extends AppCompatActivity {
    protected TextInputEditText numberOTP1, numberOTP2, numberOTP3,
            numberOTP4, numberOTP5, numberOTP6;
    protected Button confirm, cancel;
    protected TextView reSendOTP;
    protected VerifyOTPViewModel verifyOTPViewModel;
    protected String phoneNumber;
    protected Intent intentSource;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.verify_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.verify), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        verifyOTPViewModel.getPhoneNumberByUsername(
                SessionManager.getInstance(this).getAccount().getUsername()
        );

        verifyOTPViewModel.getPhoneNumberLiveData().observe(this, phoneNumberLiveData -> {
            phoneNumber = phoneNumberLiveData;

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                verifyOTPViewModel.sendOTP(phoneNumber, this);
            }, 500);

        });


        confirm.setOnClickListener(v -> {
            if(numberOTP1.getText() != null && numberOTP2.getText() != null &&
                    numberOTP3.getText() != null && numberOTP4.getText() != null &&
                    numberOTP5.getText() != null && numberOTP6.getText() != null) {

                String userOTP = numberOTP1.getText().toString() +
                        numberOTP2.getText().toString() + numberOTP3.getText().toString() +
                        numberOTP4.getText().toString() + numberOTP5.getText().toString() +
                        numberOTP6.getText().toString();

                verifyOTPViewModel.verifyOTP(userOTP);


                verifyOTPViewModel.getVerifyLiveData().observe(this, isSuccessful -> {
                    if (isSuccessful) {
                        customeAction();
                    } else {
                        Toast.makeText(this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ mã OTP", Toast.LENGTH_SHORT).show();
            }
        });


        cancel.setOnClickListener(v -> {
            finish();
        });


        reSendOTP.setOnClickListener(v -> {
            verifyOTPViewModel.sendOTP(phoneNumber, this);
        });
    }

    public void init() {
        numberOTP1 = findViewById(R.id.numberOTP1);
        numberOTP2 = findViewById(R.id.numberOTP2);
        numberOTP3 = findViewById(R.id.numberOTP3);
        numberOTP4 = findViewById(R.id.numberOTP4);
        numberOTP5 = findViewById(R.id.numberOTP5);
        numberOTP6 = findViewById(R.id.numberOTP6);

        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
        reSendOTP = findViewById(R.id.reSendOTP);

        intentSource = getIntent();

        verifyOTPViewModel = new ViewModelProvider(this).get(VerifyOTPViewModel.class);
    }


    public abstract void customeAction();
}