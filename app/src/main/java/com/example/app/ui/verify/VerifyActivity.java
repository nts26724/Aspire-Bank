package com.example.app.ui.verify;

import android.os.Bundle;
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

public abstract class VerifyActivity extends AppCompatActivity {
    private TextInputEditText numberOTP1, numberOTP2, numberOTP3,
            numberOTP4, numberOTP5, numberOTP6;
    private Button confirm, cancel;
    private TextView reSendOTP;
    private VerifyViewModel verifyViewModel;
    private String phoneNumber;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.verify);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.verify), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();


        verifyViewModel.getPhoneNumberByUsername(
                SessionManager.getInstance(this).getAccount().getUsername()
        );

        verifyViewModel.getPhoneNumberLiveData().observe(this, phoneNumberLiveData -> {
            phoneNumber = phoneNumberLiveData;
            verifyViewModel.sendOTP(phoneNumber);
        });


        confirm.setOnClickListener(v -> {
            String userOTP = numberOTP1.getText().toString() + numberOTP2.getText().toString() + numberOTP3.getText().toString() +
                    numberOTP4.getText().toString() + numberOTP5.getText().toString() + numberOTP6.getText().toString();
            verifyViewModel.verifyOTP(userOTP);

            verifyViewModel.getVerifyLiveData().observe(this, isSuccessful -> {
                if (isSuccessful) {
                    startNextIntent();
                } else {
                    Toast.makeText(this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                }
            });
        });


        cancel.setOnClickListener(v -> {
            finish();
        });


        reSendOTP.setOnClickListener(v -> {
            verifyViewModel.sendOTP(phoneNumber);
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

        verifyViewModel = new ViewModelProvider(this).get(VerifyViewModel.class);
    }


    public abstract void startNextIntent();
}