package com.example.app.ui.savings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.ui.homecustomer.HomeCustomerActivity;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SavingsSuccessActivity extends AppCompatActivity {

    private TextView tvTitle, tvMessage, tvAmount, tvTerm, tvRate, tvDateSent, tvDateEnd, tvTotal;
    private MaterialButton btnGoHome, btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savings_success_activity);

        initViews();
        displayData();
        setupListeners();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_success_title);
        tvMessage = findViewById(R.id.tv_success_message);
        tvAmount = findViewById(R.id.tv_success_amount);
        tvTerm = findViewById(R.id.tv_success_term);
        tvRate = findViewById(R.id.tv_value_rate);
        tvDateSent = findViewById(R.id.tv_success_date);
        tvDateEnd = findViewById(R.id.tv_value_date_end);
        tvTotal = findViewById(R.id.tv_value_total);
        btnGoHome = findViewById(R.id.btn_go_home);
        btnContinue = findViewById(R.id.btn_continue_saving);
    }

    private void displayData() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("TRANSACTION_TYPE");
        double amount = intent.getDoubleExtra("AMOUNT", 0);
        String term = intent.getStringExtra("TERM");
        double rate = intent.getDoubleExtra("RATE", 0);

        DecimalFormat df = new DecimalFormat("#,###");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        tvAmount.setText(df.format(amount) + " đ");
        tvTerm.setText(term);
        tvRate.setText(rate + "%/năm");
        tvDateSent.setText(sdf.format(today));

        if ("MORTGAGE".equals(type)) {
            tvTitle.setText("Giải ngân thành công!");
            tvMessage.setText("Số tiền vay đã được cộng vào tài khoản của bạn.");
        } else {
            tvTitle.setText("Gửi tiết kiệm thành công!");
            tvMessage.setText("Sổ tiết kiệm điện tử đã được khởi tạo.");
        }

        int months = 1;
        try {
            if (term != null) {
                months = Integer.parseInt(term.replace(" tháng", "").trim());
            }
        } catch (Exception e) {
            months = 1;
        }

        calendar.add(Calendar.MONTH, months);
        tvDateEnd.setText(sdf.format(calendar.getTime()));

        double interest = (amount * rate / 100) / 12 * months;
        double total = amount + interest;

        tvTotal.setText(df.format(total) + " đ");
    }

    private void setupListeners() {
        btnGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(SavingsSuccessActivity.this, HomeCustomerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(SavingsSuccessActivity.this, SavingsActivity.class);
            intent.putExtra("TRANSACTION_TYPE", "SAVINGS");
            startActivity(intent);
            finish();
        });
    }
}