package com.example.app.ui.homecustomer;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.data.model.Account;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.HomeCustomerCallback;
import com.example.app.ui.depositaccount.DepositAccountActivity;
import com.example.app.ui.depositphone.DepositPhoneActivity;
import com.example.app.ui.receiptpayment.ReceiptPaymentActivity;
import com.example.app.ui.savings.SavingsActivity;
import com.example.app.ui.transfer.TransferActivity;
import com.example.app.utils.SessionManager;

import java.text.DecimalFormat;

public class HomeCustomerActivity extends AppCompatActivity {
    TextView balance, name;
    ImageView deposit, transfer, utility, mortgage, depositPhone, saving, eye;
    private HomeCustomerViewModel homeCustomerViewModel;
    private FireStoreSource fireStoreSource;
    private boolean isBalanceHidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_customer_activity);

        init();
        displayUserData();
        setupListeners();
    }

    public void init() {
        balance = findViewById(R.id.balance);
        name = findViewById(R.id.name);

        deposit = findViewById(R.id.deposit);
        transfer = findViewById(R.id.transfer);
        utility = findViewById(R.id.utility);
        mortgage = findViewById(R.id.mortgage);
        depositPhone = findViewById(R.id.depositPhone);
        saving = findViewById(R.id.saving);
        eye = findViewById(R.id.eye);

        homeCustomerViewModel = new ViewModelProvider(this).get(HomeCustomerViewModel.class);
        fireStoreSource = new FireStoreSource();
    }

    private void displayUserData() {
        Account account = SessionManager.getInstance(this).getAccount();
        if (account != null) {
            DecimalFormat df = new DecimalFormat("#,###");
            balance.setText(df.format(account.getBalance()) + " VND");

            if (account.getUsername() != null) {
                name.setText(account.getUsername());

                fireStoreSource.getCustomerByUsername(account.getUsername(), new HomeCustomerCallback() {
                    @Override
                    public void onSuccess(String fullName) {
                        if (fullName != null && !fullName.isEmpty()) {
                            name.setText(fullName);
                        }
                    }

                    @Override
                    public void onFailure() {
                    }
                });
            }
        }
    }

    private void setupListeners() {
        eye.setOnClickListener(v -> {
            isBalanceHidden = !isBalanceHidden;
            if (isBalanceHidden) {
                balance.setTransformationMethod(new PasswordTransformationMethod());
                eye.setImageResource(R.mipmap.ic_eye_hide);
            } else {
                balance.setTransformationMethod(null);
                eye.setImageResource(R.mipmap.ic_eye);
            }
        });

        transfer.setOnClickListener(v -> {
            startActivity(new Intent(this, TransferActivity.class));
        });

        utility.setOnClickListener(v -> {
            startActivity(new Intent(this, ReceiptPaymentActivity.class));
        });

        mortgage.setOnClickListener(v -> {
            Intent intent = new Intent(this, SavingsActivity.class);
            intent.putExtra("TRANSACTION_TYPE", "MORTGAGE");
            startActivity(intent);
        });

        depositPhone.setOnClickListener(v -> {
            startActivity(new Intent(this, DepositPhoneActivity.class));
        });

        saving.setOnClickListener(v -> {
            Intent intent = new Intent(this, SavingsActivity.class);
            intent.putExtra("TRANSACTION_TYPE", "SAVINGS");
            startActivity(intent);
        });

        deposit.setOnClickListener(v -> {
            startActivity(new Intent(this, DepositAccountActivity.class));
        });
    }
}