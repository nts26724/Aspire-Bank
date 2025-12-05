package com.example.app.ui.depositaccount;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;

import java.text.NumberFormat;
import java.util.Locale;

public class DepositAccountCheck extends AppCompatActivity {
    private TextView linkedBank, numberAccount, nameAccount, amount,
            pay, cancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.deposit_account_check);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.deposit_account_check), (v, insets) -> {
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


        pay.setOnClickListener(v -> {
            Intent intentDepositAccountVerifyOTP = new Intent(DepositAccountCheck.this, DepositAccountVerifyOTP.class);
            intentDepositAccountVerifyOTP.putExtra("linkedBank", linkedBankStr);
            intentDepositAccountVerifyOTP.putExtra("numberAccount", numberAccountStr);
            intentDepositAccountVerifyOTP.putExtra("nameAccount", nameAccountStr);
            intentDepositAccountVerifyOTP.putExtra("amount", amountStr);
            startActivity(intentDepositAccountVerifyOTP);
        });


        cancel.setOnClickListener(v -> {
            finish();
        });
    }


    public void init() {
        linkedBank = findViewById(R.id.linkedBank);
        numberAccount = findViewById(R.id.numberAccount);
        nameAccount = findViewById(R.id.nameAccount);
        amount = findViewById(R.id.amount);

        pay = findViewById(R.id.pay);
        cancel = findViewById(R.id.cancel);
    }
}
