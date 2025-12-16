package com.example.app.ui.savings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.app.R;
import com.example.app.ui.verify.VerifyOTPActivity;
import com.example.app.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class SavingsVerifyOTP extends VerifyOTPActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btnVerify = findViewById(R.id.confirm);

        TextInputEditText et1 = findViewById(R.id.numberOTP1);
        TextInputEditText et2 = findViewById(R.id.numberOTP2);
        TextInputEditText et3 = findViewById(R.id.numberOTP3);
        TextInputEditText et4 = findViewById(R.id.numberOTP4);
        TextInputEditText et5 = findViewById(R.id.numberOTP5);
        TextInputEditText et6 = findViewById(R.id.numberOTP6);

        if (btnVerify != null) {
            btnVerify.setOnClickListener(v -> {
                StringBuilder otpBuilder = new StringBuilder();
                if (et1 != null) otpBuilder.append(et1.getText().toString());
                if (et2 != null) otpBuilder.append(et2.getText().toString());
                if (et3 != null) otpBuilder.append(et3.getText().toString());
                if (et4 != null) otpBuilder.append(et4.getText().toString());
                if (et5 != null) otpBuilder.append(et5.getText().toString());
                if (et6 != null) otpBuilder.append(et6.getText().toString());

                String otpInput = otpBuilder.toString().trim();

                if ("123456".equals(otpInput)) {
                    customeAction();
                } else {
                    Toast.makeText(this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void customeAction() {
        String transactionType = intentSource.getStringExtra("TRANSACTION_TYPE");
        double amountDouble = intentSource.getDoubleExtra("AMOUNT", 0);
        long amount = (long) amountDouble;
        String term = intentSource.getStringExtra("TERM");
        double rate = intentSource.getDoubleExtra("RATE", 0);

        String username = SessionManager.getInstance(this).getAccount().getUsername();

        if ("MORTGAGE".equals(transactionType)) {
            verifyOTPViewModel.deposit(username, amount, "MORTGAGE");

            verifyOTPViewModel.getDepositLiveData().observe(this, isSuccessful -> {
                if (isSuccessful) {
                    navigateToSuccess(transactionType, amountDouble, term, rate);
                } else {
                    Toast.makeText(this, "Giao dịch thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            verifyOTPViewModel.widthraw(
                    username,
                    amount,
                    "SAVINGS - Term: " + term,
                    "AspireBank"
            );

            verifyOTPViewModel.getBooleanLiveData().observe(this, isSuccessful -> {
                if (isSuccessful) {
                    navigateToSuccess(transactionType, amountDouble, term, rate);
                } else {
                    Toast.makeText(this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToSuccess(String type, double amount, String term, double rate) {
        Intent intent = new Intent(this, SavingsSuccessActivity.class);
        intent.putExtra("TRANSACTION_TYPE", type);
        intent.putExtra("AMOUNT", amount);
        intent.putExtra("TERM", term);
        intent.putExtra("RATE", rate);
        startActivity(intent);
        finish();
    }
}