package com.example.app.ui.depositaccount;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.google.android.material.textfield.TextInputEditText;

public class DepositAccountActivity extends AppCompatActivity {
    private TextInputEditText linkedBank, numberAccount, nameAccount, amount;
    private TextView deposit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.deposit_account_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.deposit_account), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();


        deposit.setOnClickListener(v -> {
            String linkedBankStr = linkedBank.getText().toString().trim();
            String numberAccountStr = numberAccount.getText().toString().trim();
            String nameAccountStr = nameAccount.getText().toString().trim();
            String amountStr = amount.getText().toString().trim();

            if(linkedBankStr.isEmpty() || numberAccountStr.isEmpty() ||
                    nameAccountStr.isEmpty() || amountStr.isEmpty()) {

                Toast.makeText(this,
                        "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();

            } else if(Long.parseLong(amountStr) < 50000) {

                Toast.makeText(this,
                        "Số tiền tối thiểu là 50.000 đ", Toast.LENGTH_SHORT).show();

            } else {
                Intent intentDepositAccountCheck = new Intent(DepositAccountActivity.this, DepositAccountCheck.class);
                intentDepositAccountCheck.putExtra("linkedBank", linkedBankStr);
                intentDepositAccountCheck.putExtra("numberAccount", numberAccountStr);
                intentDepositAccountCheck.putExtra("nameAccount", nameAccountStr);
                Log.d("amount", "amount: " + amountStr);
                intentDepositAccountCheck.putExtra("amount", amountStr);
                startActivity(intentDepositAccountCheck);
            }
        });
    }


    public void init() {
        linkedBank = findViewById(R.id.linkedBank);
        numberAccount = findViewById(R.id.numberAccount);
        nameAccount = findViewById(R.id.nameAccount);
        amount = findViewById(R.id.amount);

        deposit = findViewById(R.id.deposit);
    }
}
