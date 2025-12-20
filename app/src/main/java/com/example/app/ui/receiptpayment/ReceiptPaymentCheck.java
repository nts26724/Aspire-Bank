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

import java.text.NumberFormat;
import java.util.Locale;

public class ReceiptPaymentCheck extends AppCompatActivity {
    private TextView type, amount, receiptID;
    private Button payment, cancel;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.receipt_payment_check);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.receipt_payment_check), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();


        Intent intent = getIntent();
        String typeStr = intent.getStringExtra("type");
        String amountStr = intent.getStringExtra("amount");
        String receiptIDStr = intent.getStringExtra("receiptID");

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        type.setText(typeStr);
        amount.setText(formatter.format(amount));
        receiptID.setText(receiptIDStr);

        payment.setOnClickListener(v -> {
            Intent intentReceiptPaymentVerify = new Intent(this, ReceiptPaymentVerifyOTP.class);
            intentReceiptPaymentVerify.putExtra("type", typeStr);
            intentReceiptPaymentVerify.putExtra("amount", amountStr);
            intentReceiptPaymentVerify.putExtra("receiptID", receiptIDStr);
            startActivity(intentReceiptPaymentVerify);
        });

        cancel.setOnClickListener(v -> {
            finish();
        });
    }

    public void init() {
        type = findViewById(R.id.type);
        amount = findViewById(R.id.amount);
        receiptID = findViewById(R.id.receiptID);
        payment = findViewById(R.id.payment);
        cancel = findViewById(R.id.cancel);
    }
}
