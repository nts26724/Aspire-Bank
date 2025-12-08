package com.example.app.ui.savings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.ui.homecustomer.HomeCustomerActivity;
import com.example.app.ui.customeview.AppBarView;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SavingsSuccessActivity extends AppCompatActivity {

    private TextView tvAccount, tvAmount, tvTerm, tvRate, tvDateSent, tvDateEnd, tvTotal;
    private MaterialButton btnContinue, btnHome;
    private AppBarView appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savings_success_activity);

        initViews();
        displayDataFromIntent();
        setupListeners();
    }

    private void initViews() {
        appBar = findViewById(R.id.app_bar);
        tvAccount = findViewById(R.id.tv_value_account);
        tvAmount = findViewById(R.id.tv_value_amount);
        tvTerm = findViewById(R.id.tv_value_term);
        tvRate = findViewById(R.id.tv_value_rate);
        tvDateSent = findViewById(R.id.tv_value_date_sent);
        tvDateEnd = findViewById(R.id.tv_value_date_end);
        tvTotal = findViewById(R.id.tv_value_total);

        btnContinue = findViewById(R.id.btn_continue_saving);
        btnHome = findViewById(R.id.btn_home);
    }

    private void displayDataFromIntent() {
        Intent intent = getIntent();
        double amount = intent.getDoubleExtra("AMOUNT", 0);
        String term = intent.getStringExtra("TERM");
        double rate = intent.getDoubleExtra("RATE", 0);

        DecimalFormat df = new DecimalFormat("#,###");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        if (tvAccount != null) tvAccount.setText("0123456789");
        if (tvAmount != null) tvAmount.setText(df.format(amount) + " đ");
        if (tvTerm != null) tvTerm.setText(term);
        if (tvRate != null) tvRate.setText(rate + "%/năm");

        if (tvDateSent != null) tvDateSent.setText(sdf.format(calendar.getTime()));

        int months = 1;
        try {
            months = Integer.parseInt(term.replace(" tháng", "").trim());
        } catch (Exception e) { months = 1; }
        calendar.add(Calendar.MONTH, months);
        if (tvDateEnd != null) tvDateEnd.setText(sdf.format(calendar.getTime()));

        double interest = (amount * rate / 100) / 12 * months;
        if (tvTotal != null) tvTotal.setText(df.format(amount + interest) + " đ");
    }

    private void setupListeners() {
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(SavingsSuccessActivity.this, HomeCustomerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(SavingsSuccessActivity.this, HomeCustomerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}