package com.example.app.ui.depositphone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.example.app.ui.homecustomer.HomeCustomerActivity;

public class DepositPhoneSuccess extends AppCompatActivity {
    private TextView phoneNumber, amount, continueDepositPhone, homeCustomer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.deposit_phone_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.deposit_phone_success), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Intent intent = getIntent();
        String phoneNumberStr = intent.getStringExtra("phoneNumber");
        String amountStr = intent.getStringExtra("amount");

        phoneNumber.setText(phoneNumberStr);
        amount.setText(amountStr);

        continueDepositPhone.setOnClickListener(v -> {
            Intent intentDepositPhone = new Intent(this, DepositPhoneActivity.class);
            startActivity(intentDepositPhone);
        });

        homeCustomer.setOnClickListener(v -> {
            Intent intentHomeCustomer = new Intent(this, HomeCustomerActivity.class);
            startActivity(intentHomeCustomer);
        });
    }

    public void init() {
        phoneNumber = findViewById(R.id.phoneNumber);
        amount = findViewById(R.id.amount);
        continueDepositPhone = findViewById(R.id.continueDepositPhone);
        homeCustomer = findViewById(R.id.homeCustomer);
    }
}
