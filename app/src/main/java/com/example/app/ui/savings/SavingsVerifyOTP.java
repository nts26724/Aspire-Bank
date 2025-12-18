package com.example.app.ui.savings;

import android.content.Intent;
import android.widget.Toast;

import com.example.app.ui.verify.VerifyOTPActivity;
import com.example.app.utils.SessionManager;

public class SavingsVerifyOTP extends VerifyOTPActivity {

    @Override
    public void customeAction() {
        Intent intent = getIntent();

        String transactionType = intent.getStringExtra("TRANSACTION_TYPE");
        double amountDouble = intent.getDoubleExtra("AMOUNT", 0);
        long amount = (long) amountDouble;
        String term = intent.getStringExtra("TERM");
        double rate = intent.getDoubleExtra("RATE", 0);

        String username = SessionManager.getInstance(this).getAccount().getUsername();

        if ("MORTGAGE".equals(transactionType)) {
            verifyOTPViewModel.deposit(username, amount, "MORTGAGE");

            verifyOTPViewModel.getDepositLiveData().observe(this, isSuccessful -> {
                if (isSuccessful) {
                    navigateToSuccess(transactionType, amountDouble, term, rate);
                } else {
                    Toast.makeText(this, "Giao dịch vay vốn thất bại", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Số dư không đủ để gửi tiết kiệm", Toast.LENGTH_SHORT).show();
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