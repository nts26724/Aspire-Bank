package com.example.app.ui.savings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.ui.verify.VerifyOTPActivity;
import com.google.android.material.button.MaterialButton;
import com.example.app.ui.customeview.AppBarView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SavingsConfirmActivity extends AppCompatActivity {

    private TextView tvAccount, tvAmount, tvTerm, tvRate, tvDateSent, tvDateEnd, tvTotal;
    private MaterialButton btnConfirm, btnCancel;
    private AppBarView appBar;

    private double amount;
    private String term;
    private double rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savings_confirm_activity);

        Intent intent = getIntent();
        amount = intent.getDoubleExtra("AMOUNT", 0);
        term = intent.getStringExtra("TERM");
        rate = intent.getDoubleExtra("RATE", 0);

        initViews();
        displayData();
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

        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void displayData() {
        DecimalFormat df = new DecimalFormat("#,###");

        if (tvAccount != null) tvAccount.setText("0123456789");
        if (tvAmount != null) tvAmount.setText(df.format(amount) + " đ");
        if (tvTerm != null) tvTerm.setText(term);
        if (tvRate != null) tvRate.setText(rate + "%/năm");

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (tvDateSent != null) tvDateSent.setText(sdf.format(today));

        int months = 1;
        try {
            months = Integer.parseInt(term.replace(" tháng", "").trim());
        } catch (Exception e) { months = 1; }

        calendar.add(Calendar.MONTH, months);
        Date endDate = calendar.getTime();
        if (tvDateEnd != null) tvDateEnd.setText(sdf.format(endDate));

        double interest = (amount * rate / 100) / 12 * months;
        double total = amount + interest;
        if (tvTotal != null) tvTotal.setText(df.format(total) + " đ");
    }

    private void setupListeners() {
        btnConfirm.setOnClickListener(v -> {
            Intent intent = new Intent(SavingsConfirmActivity.this, VerifyOTPActivity.class);

            intent.putExtra("NEXT_SCREEN", "SAVINGS_SUCCESS");
            intent.putExtra("AMOUNT", amount);
            intent.putExtra("TERM", term);
            intent.putExtra("RATE", rate);

            startActivity(intent);
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}