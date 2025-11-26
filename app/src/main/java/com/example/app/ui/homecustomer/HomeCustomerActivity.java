package com.example.app.ui.homecustomer;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.utils.SessionManager;

public class HomeCustomerActivity extends AppCompatActivity {
    TextView balance, name;
    ImageView deposit, transfer, utility, mortgage, depositPhone, saving;
    private HomeCustomerViewModel homeCustomerViewModel;

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

        homeCustomerViewModel.setAccountLiveData(SessionManager.getInstance().getUser());

        homeCustomerViewModel.getAccountLiveData().observe(this, account -> {
            name.setText(account.getName());
            balance.setText(account.getBalance() + " VND");
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

        homeCustomerViewModel = new ViewModelProvider(this).get(HomeCustomerViewModel.class);
    }
}
