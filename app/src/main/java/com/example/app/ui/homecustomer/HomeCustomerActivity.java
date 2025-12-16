package com.example.app.ui.homecustomer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.data.model.Account;
import com.example.app.ui.depositaccount.DepositAccountActivity;
import com.example.app.ui.depositphone.DepositPhoneActivity;
import com.example.app.ui.receiptpayment.ReceiptPaymentActivity;
import com.example.app.ui.savings.SavingsActivity;
import com.example.app.utils.SessionManager;

import java.text.NumberFormat;
import java.util.Locale;

public class HomeCustomerActivity extends AppCompatActivity {
    TextView balance, name;
    ImageView deposit, transfer, utility, mortgage, depositPhone, saving, eye;
    private HomeCustomerViewModel homeCustomerViewModel;
    private MutableLiveData<Boolean> hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_customer_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_customer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        eye.setOnClickListener(v -> {
            hide.postValue(Boolean.FALSE.equals(hide.getValue()));
        });

        hide.observe(this, isHidden -> {
            Account account = SessionManager.getInstance(this).getAccount();
            if (account == null) {
                balance.setText("Unknown");
                return;
            }
            eye.setImageResource(isHidden ? R.mipmap.ic_eye_round : R.mipmap.ic_eye_hide);

            if (isHidden) {
                balance.setText("****** VND");
            } else {
                NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                balance.setText(formatter.format(account.getBalance()) + " VND");
            }
        });

        homeCustomerViewModel.setAccountLiveData(SessionManager.getInstance(this).getAccount());

        homeCustomerViewModel.getAccountLiveData().observe(this, account -> {
            if (Boolean.TRUE.equals(hide.getValue())) {
                balance.setText("****** VND");
            } else {
                NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                balance.setText(formatter.format(account.getBalance()) + " VND");
            }
        });

        homeCustomerViewModel.getFullNameByUsername(
                SessionManager.getInstance(this).getAccount().getUsername()
        );

        homeCustomerViewModel.getFullNameLiveData().observe(this, fullName -> {
            if (fullName == null) {
                name.setText("Unknown");
                return;
            }
            name.setText(fullName);
        });

        deposit.setOnClickListener(v -> {
            Intent intent = new Intent(this, DepositAccountActivity.class);
            startActivity(intent);
        });

        transfer.setOnClickListener(v -> {});

        utility.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReceiptPaymentActivity.class);
            startActivity(intent);
        });

        mortgage.setOnClickListener(v -> {
            Intent intent = new Intent(this, SavingsActivity.class);
            intent.putExtra("TRANSACTION_TYPE", "MORTGAGE");
            startActivity(intent);
        });

        depositPhone.setOnClickListener(v -> {
            Intent intent = new Intent(this, DepositPhoneActivity.class);
            startActivity(intent);
        });

        saving.setOnClickListener(v -> {
            Intent intent = new Intent(this, SavingsActivity.class);
            intent.putExtra("TRANSACTION_TYPE", "SAVINGS");
            startActivity(intent);
        });
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

        hide = new MutableLiveData<>(false);
        homeCustomerViewModel = new ViewModelProvider(this).get(HomeCustomerViewModel.class);
    }
}