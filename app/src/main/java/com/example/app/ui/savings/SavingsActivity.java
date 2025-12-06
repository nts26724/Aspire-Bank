package com.example.app.ui.savings;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.ui.customeview.AppBarView;
import com.example.app.ui.customeview.NavBarView;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SavingsActivity extends AppCompatActivity {

    private AppBarView appBar;
    private NavBarView navBar;
    private EditText etAmount;
    private MaterialButton btnSubmit;

    private Button btn5M, btn10M, btn50M;

    private TextView tvTermLabel, tvInterestRateLabel;
    private TextView tvPrincipal, tvInterestAmount, tvTotalAmount, tvNote;

    private List<MaterialButton> termButtons = new ArrayList<>();

    private double currentAmount = 0;
    private String selectedTerm = "1 tháng";
    private double currentInterestRate = 3.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savings_fragment);

        initViews();
        setupTermButtons();
        setupQuickAmountButtons();
        setupAmountInput();
        setupSubmitButton();
    }

    private void initViews() {
        appBar = findViewById(R.id.app_bar);
        navBar = findViewById(R.id.nav_bar);
        btnSubmit = findViewById(R.id.btn_submit_savings);
        etAmount = findViewById(R.id.et_savings_amount);

        btn5M = findViewById(R.id.btn_5m);
        btn10M = findViewById(R.id.btn_10m);
        btn50M = findViewById(R.id.btn_50m);

        tvTermLabel = findViewById(R.id.tv_term_value);
        tvInterestRateLabel = findViewById(R.id.tv_rate_value);
        tvPrincipal = findViewById(R.id.tv_principal_value);
        tvInterestAmount = findViewById(R.id.tv_interest_value);
        tvTotalAmount = findViewById(R.id.tv_total_value);
        tvNote = findViewById(R.id.tv_note);

        GridLayout gridLayout = findViewById(R.id.grid_terms);
        if (gridLayout != null) {
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                View child = gridLayout.getChildAt(i);
                if (child instanceof MaterialButton) {
                    termButtons.add((MaterialButton) child);
                }
            }
        }
    }

    private void setupTermButtons() {
        for (MaterialButton btn : termButtons) {
            btn.setOnClickListener(v -> {
                for (MaterialButton b : termButtons) {
                    b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F5F5F5")));
                }
                btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF3E0")));

                String text = btn.getText().toString();
                String[] parts = text.split("\n");
                if (parts.length >= 2) {
                    selectedTerm = parts[0];
                    String rateString = parts[1].replace("%/năm", "");
                    currentInterestRate = Double.parseDouble(rateString);
                }

                updateCalculations();
            });
        }
    }

    private void setupQuickAmountButtons() {
        View.OnClickListener quickAddListener = v -> {
            Button b = (Button) v;
            String text = b.getText().toString();
            double addAmount = 0;
            if (text.equals("5M")) addAmount = 5000000;
            else if (text.equals("10M")) addAmount = 10000000;
            else if (text.equals("50M")) addAmount = 50000000;

            currentAmount = addAmount;
            etAmount.setText(String.format("%.0f", currentAmount));
        };

        if(btn5M != null) btn5M.setOnClickListener(quickAddListener);
        if(btn10M != null) btn10M.setOnClickListener(quickAddListener);
        if(btn50M != null) btn50M.setOnClickListener(quickAddListener);
    }

    private void setupAmountInput() {
        if(etAmount == null) return;

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String cleanString = s.toString().replaceAll("[,.]", "").trim();
                    if (!cleanString.isEmpty()) {
                        currentAmount = Double.parseDouble(cleanString);
                    } else {
                        currentAmount = 0;
                    }
                    updateCalculations();
                } catch (NumberFormatException e) {
                    currentAmount = 0;
                }
            }
        });
    }

    private void updateCalculations() {
        DecimalFormat df = new DecimalFormat("#,###");

        int months = 1;
        try {
            months = Integer.parseInt(selectedTerm.replace(" tháng", "").trim());
        } catch (Exception e) { months = 1; }

        double interest = (currentAmount * currentInterestRate / 100) / 12 * months;
        double total = currentAmount + interest;

        if (tvTermLabel != null) tvTermLabel.setText(selectedTerm);
        if (tvInterestRateLabel != null) tvInterestRateLabel.setText(currentInterestRate + "%/năm");
        if (tvPrincipal != null) tvPrincipal.setText(df.format(currentAmount) + " đ");
        if (tvInterestAmount != null) tvInterestAmount.setText("+" + df.format(interest) + " đ");
        if (tvTotalAmount != null) tvTotalAmount.setText(df.format(total) + " đ");

        if (tvNote != null) {
            tvNote.setText("Sau " + selectedTerm + ", bạn sẽ nhận được " + df.format(total) + "đ khi đáo hạn");
        }
    }

    private void setupSubmitButton() {
        btnSubmit.setOnClickListener(v -> {
            if (currentAmount < 1000000) {
                Toast.makeText(this, "Số tiền tối thiểu là 1.000.000đ", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(SavingsActivity.this, SavingsConfirmActivity.class);
            intent.putExtra("AMOUNT", currentAmount);
            intent.putExtra("TERM", selectedTerm);
            intent.putExtra("RATE", currentInterestRate);
            startActivity(intent);
        });
    }
}