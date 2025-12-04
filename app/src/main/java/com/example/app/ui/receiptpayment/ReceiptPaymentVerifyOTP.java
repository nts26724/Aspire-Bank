package com.example.app.ui.receiptpayment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.ui.verify.VerifyOTPActivity;
import com.example.app.ui.verify.VerifyOTPViewModel;
import com.example.app.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class ReceiptPaymentVerifyOTP extends VerifyOTPActivity {
    public void customeAction() {
        long amountLong = Long.parseLong(intentSource.getStringExtra("amount"));
        String username = SessionManager.getInstance(this).getAccount().getUsername();
        String receiptID = intentSource.getStringExtra("receiptID");
        String type = intentSource.getStringExtra("type");

        verifyOTPViewModel.widthraw(username, amountLong, type, "EVN");

        verifyOTPViewModel.getBooleanLiveData().observe(this, isSuccessful -> {
                if(isSuccessful) {
                    Intent intentReceiptPaymentSuccess = new Intent(this, ReceiptPaymentSuccess.class);
                    intentReceiptPaymentSuccess.putExtra("type", type);
                    intentReceiptPaymentSuccess.putExtra("amount", intentSource.getStringExtra("amount"));
                    intentReceiptPaymentSuccess.putExtra("receiptID", receiptID);
                    startActivity(intentReceiptPaymentSuccess);
                } else {
                    Toast.makeText(this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
                }
        });
    }
}
