package com.example.app.ui.depositphone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.example.app.ui.verify.VerifyOTPActivity;
import com.example.app.utils.SessionManager;

public class DepositPhoneVerifyOTP extends VerifyOTPActivity {
    public void customeAction() {
        String username = SessionManager.getInstance(this).getAccount().getUsername();
        String phoneNumberStr = intentSource.getStringExtra("phoneNumber");
        String amountStr = intentSource.getStringExtra("amount");
        long amountLong = Long.parseLong(amountStr);

        verifyOTPViewModel.widthraw(username, amountLong,
                "DepositPhone", "Telecommunications Company");

        verifyOTPViewModel.getBooleanLiveData().observe(this, isSuccessful -> {
            if(isSuccessful) {
                Intent intentDepositPhoneSuccess = new Intent(this, DepositPhoneSuccess.class);
                intentDepositPhoneSuccess.putExtra("phoneNumber", phoneNumberStr);
                intentDepositPhoneSuccess.putExtra("amount", amountStr);
                startActivity(intentDepositPhoneSuccess);
            } else {
                Toast.makeText(this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
