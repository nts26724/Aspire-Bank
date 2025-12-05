package com.example.app.ui.depositaccount;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.app.ui.bookticket.BookTicketSuccess;
import com.example.app.ui.verify.VerifyOTPActivity;
import com.example.app.utils.SessionManager;

public class DepositAccountVerifyOTP extends VerifyOTPActivity {
    public void customeAction() {
        String linkedBankStr = intentSource.getStringExtra("linkedBank");
        String numberAccountStr = intentSource.getStringExtra("numberAccount");
        String nameAccountStr = intentSource.getStringExtra("nameAccount");
        String amountStr = intentSource.getStringExtra("amount");


        verifyOTPViewModel.deposit(
                SessionManager.getInstance(this).getAccount().getUsername(),
                Long.parseLong(amountStr)
        );


        verifyOTPViewModel.getDepositLiveData().observe(this, isSuccessful -> {
            if(isSuccessful) {
                Intent intentDepositAccountSuccess = new Intent(this, DepositAccountSuccess.class);
                intentDepositAccountSuccess.putExtra("linkedBank", linkedBankStr);
                intentDepositAccountSuccess.putExtra("numberAccount", numberAccountStr);
                intentDepositAccountSuccess.putExtra("nameAccount", nameAccountStr);
                intentDepositAccountSuccess.putExtra("amount", amountStr);
                startActivity(intentDepositAccountSuccess);
            } else {
                Log.d("obserBookingLiveData", "if false");
                Toast.makeText(this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
