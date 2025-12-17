package com.example.app.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.example.app.R;
import com.example.app.ui.homecustomer.HomeCustomerActivity;
import com.example.app.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;

public class TransferSuccessActivity extends AppCompatActivity {

    private TextView tvSenderName;
    private TextView tvReceiverName;
    private TextView tvBankName;
    private TextView tvAmount;
    private TextView tvContent;
    private Group groupBankDetails;

    private MaterialButton btnContinue;
    private MaterialButton btnSaveImage;
    private MaterialButton btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_success_activity);

        initViews();
        displayData();
        setupListeners();
    }

    private void initViews() {
        tvSenderName = findViewById(R.id.tv_sender_name);
        tvReceiverName = findViewById(R.id.tv_receiver_name);
        tvBankName = findViewById(R.id.tv_bank_name);
        tvAmount = findViewById(R.id.tv_amount);
        tvContent = findViewById(R.id.tv_content);
        groupBankDetails = findViewById(R.id.group_bank_details);

        btnContinue = findViewById(R.id.btn_continue_transaction);
        btnSaveImage = findViewById(R.id.btn_save_image);
        btnHome = findViewById(R.id.btn_go_home);
    }

    private void displayData() {
        Intent intent = getIntent();
        String transactionType = intent.getStringExtra("TRANSACTION_TYPE");
        double amount = intent.getDoubleExtra("AMOUNT", 0);
        String receiverName = intent.getStringExtra("RECEIVER_NAME");
        String bankName = intent.getStringExtra("BANK_NAME");
        String message = intent.getStringExtra("MESSAGE");

        String senderName = intent.getStringExtra("SENDER_NAME");
        if (senderName != null) {
            tvSenderName.setText(senderName);
        } else {
            tvSenderName.setText(SessionManager.getInstance(this).getAccount().getUsername());
        }

        if (receiverName != null) {
            tvReceiverName.setText(receiverName);
        }

        DecimalFormat df = new DecimalFormat("#,###");
        tvAmount.setText(df.format(amount) + " VND");

        if (message != null && !message.isEmpty()) {
            tvContent.setText(message);
        } else {
            tvContent.setText("Chuyển tiền");
        }

        if ("TRANSFER_INTERNAL".equals(transactionType)) {
            groupBankDetails.setVisibility(View.GONE);
        } else {
            groupBankDetails.setVisibility(View.VISIBLE);
            if (bankName != null) {
                tvBankName.setText(bankName);
            }
        }
    }

    private void setupListeners() {
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(TransferSuccessActivity.this, HomeCustomerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(TransferSuccessActivity.this, TransferActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnSaveImage.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }
}