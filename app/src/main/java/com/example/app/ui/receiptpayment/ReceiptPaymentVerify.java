package com.example.app.ui.receiptpayment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.google.android.material.textfield.TextInputEditText;

public class ReceiptPaymentVerify extends AppCompatActivity {
    TextInputEditText numberOTP1, numberOTP2, numberOTP3, numberOTP4, numberOTP5, numberOTP6;
    Button confirm, cancel;
    TextView reSendOTP;



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

        Intent intent = getIntent();
        String typeStr = intent.getStringExtra("type");
        String amountStr = intent.getStringExtra("amount");
        String receiptIDStr = intent.getStringExtra("receiptID");



        confirm.setOnClickListener(v -> {
            //check
            // if(false)
                Toast.makeText(this, "Sai mã OTP", Toast.LENGTH_SHORT).show();
            // if(true)
                Intent intentReceiptPaymentSuccess = new Intent(this, ReceiptPaymentSuccess.class);
                intentReceiptPaymentSuccess.putExtra("type", typeStr);
                intentReceiptPaymentSuccess.putExtra("amount", amountStr);
                intentReceiptPaymentSuccess.putExtra("receiptID", receiptIDStr);
                startActivity(intentReceiptPaymentSuccess);
        });


        cancel.setOnClickListener(v -> {
            finish();
        });


        reSendOTP.setOnClickListener(v -> {

        });

    }

    public void init() {
        numberOTP1 = findViewById(R.id.numberOTP1);
        numberOTP2 = findViewById(R.id.numberOTP2);
        numberOTP3 = findViewById(R.id.numberOTP3);
        numberOTP4 = findViewById(R.id.numberOTP4);
        numberOTP5 = findViewById(R.id.numberOTP5);
        numberOTP6 = findViewById(R.id.numberOTP6);

        reSendOTP = findViewById(R.id.reSendOTP);

        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);

    }
}
