package com.example.app.ui.depositaccount;

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

import java.text.NumberFormat;
import java.util.Locale;

public class DepositAccountSuccess extends AppCompatActivity {
    private TextView linkedBank, numberAccount, nameAccount, amount,
            continueDepositAccount, homeCustomer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.deposit_account_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.deposit_account_success), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Intent intentSource = getIntent();

        String linkedBankStr = intentSource.getStringExtra("linkedBank");
        String numberAccountStr = intentSource.getStringExtra("numberAccount");
        String nameAccountStr = intentSource.getStringExtra("nameAccount");
        String amountStr = intentSource.getStringExtra("amount");

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        linkedBank.setText(linkedBankStr);
        numberAccount.setText(numberAccountStr);
        nameAccount.setText(nameAccountStr);
        amount.setText(formatter.format(Long.parseLong(amountStr)) + " đ");


        continueDepositAccount.setOnClickListener(v -> {
            Intent intentDepositAccountActivity = new Intent(this, DepositAccountActivity.class);
            startActivity(intentDepositAccountActivity);
        });


        homeCustomer.setOnClickListener(v -> {
            Intent intentHomeCustomer = new Intent(this, HomeCustomerActivity.class);
            startActivity(intentHomeCustomer);
        });
    }


    public void init() {
        linkedBank = findViewById(R.id.linkedBank);
        numberAccount = findViewById(R.id.numberAccount);
        nameAccount = findViewById(R.id.nameAccount);
        amount = findViewById(R.id.amount);

        continueDepositAccount = findViewById(R.id.continueDepositAccount);
        homeCustomer = findViewById(R.id.homeCustomer);
    }
}
