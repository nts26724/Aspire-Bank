package com.example.app.ui.transfer;

import android.content.Intent;
import android.widget.Toast;

import com.example.app.ui.verify.VerifyOTPActivity;
import com.example.app.utils.SessionManager;

public class TransferVerifyOTP extends VerifyOTPActivity {

    @Override
    public void customeAction() {
        String username = SessionManager.getInstance(this).getAccount().getUsername();
        String senderName = intentSource.getStringExtra("SENDER_NAME");

        String transactionType = intentSource.getStringExtra("TRANSACTION_TYPE");
        double amountDouble = intentSource.getDoubleExtra("AMOUNT", 0);
        long amount = (long) amountDouble;
        String message = intentSource.getStringExtra("MESSAGE");

        String receiverAccount = intentSource.getStringExtra("RECEIVER_ACCOUNT");
        String receiverName = intentSource.getStringExtra("RECEIVER_NAME");
        String bankName = intentSource.getStringExtra("BANK_NAME");

        if ("TRANSFER_INTERNAL".equals(transactionType)) {
            verifyOTPViewModel.transferInternal(username, receiverAccount, amount, message);
        } else {
            String content = "Chuyển tới " + bankName + " - " + receiverAccount + ": " + message;
            verifyOTPViewModel.widthraw(username, amount, content, "AspireBank");
        }

        verifyOTPViewModel.getBooleanLiveData().observe(this, isSuccess -> {
            if (isSuccess) {
                Intent intent = new Intent(this, TransferSuccessActivity.class);
                intent.putExtra("TRANSACTION_TYPE", transactionType);
                intent.putExtra("AMOUNT", amountDouble);
                intent.putExtra("RECEIVER_NAME", receiverName);
                intent.putExtra("SENDER_NAME", senderName);

                String detail = "TRANSFER_INTERNAL".equals(transactionType)
                        ? receiverAccount
                        : (bankName + " - " + receiverAccount);
                intent.putExtra("RECEIVER_DETAIL", detail);

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Số dư không đủ hoặc lỗi hệ thống", Toast.LENGTH_SHORT).show();
            }
        });
    }
}