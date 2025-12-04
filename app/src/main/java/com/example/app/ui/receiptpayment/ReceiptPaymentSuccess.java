package com.example.app.ui.receiptpayment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.example.app.ui.homecustomer.HomeCustomerActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class ReceiptPaymentSuccess extends AppCompatActivity {
    private TextView type, amount, receiptID;
    Button continuePayment, homeCustomer;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.receipt_payment_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.receipt_payment_success), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Intent intent = getIntent();
        String typeStr = intent.getStringExtra("type");
        String amountStr = intent.getStringExtra("amount");
        String receiptIDStr = intent.getStringExtra("receiptID");


        type.setText(typeStr);
        amount.setText(amountStr);
        receiptID.setText(receiptIDStr);

        continuePayment.setOnClickListener(v -> {
            Intent intentReceiptPaymentActivity = new Intent(this, ReceiptPaymentActivity.class);
            startActivity(intentReceiptPaymentActivity);
        });

        homeCustomer.setOnClickListener(v -> {
            Intent intentHomeCustomerActivity = new Intent(this, HomeCustomerActivity.class);
            startActivity(intentHomeCustomerActivity);
        });
    }

    public void init() {
        type = findViewById(R.id.type);
        amount = findViewById(R.id.amount);
        receiptID = findViewById(R.id.receiptID);

        continuePayment = findViewById(R.id.continuePayment);
        homeCustomer = findViewById(R.id.homeCustomer);
    }
}
