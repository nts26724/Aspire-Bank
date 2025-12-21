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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.data.model.Account;
import com.example.app.data.model.InterestRate;
import com.example.app.data.repository.RateRepository;
import com.example.app.interfaces.RateCallback;
import com.example.app.ui.customeview.AppBarView;
import com.example.app.ui.customeview.NavBarView;
import com.example.app.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SavingsActivity extends AppCompatActivity {

    private AppBarView appBar;
    private NavBarView navBar;
    private EditText etAmount;
    private MaterialButton btnSubmit;
    private Button btn5M, btn10M, btn50M;
    private TextView tvScreenTitle, tvTermLabel, tvInterestRateLabel;
    private TextView tvPrincipal, tvInterestAmount, tvTotalAmount, tvNote, tvTotalLabel;

    private TextView tvLabelInputAmount, tvLabelChooseTerm, tvMonthlyPaymentValue;
    private LinearLayout layoutMonthlyPayment;

    private TextView tvSourceAccountNumber, tvSourceBalance;
    private double sourceAccountBalance = 0.0;

    private String currentAccountId = null;

    private List<MaterialButton> termButtons = new ArrayList<>();
    private double currentAmount = 0;
    private String selectedTermString = "1 tháng";
    private int selectedTermMonths = 1;

    private double baseInterestRate = 0.0;
    private double currentInterestRate = 0.0;

    private String transactionType = "SAVINGS";
    private RateRepository rateRepository;

    private final Locale localeVN = new Locale("vi", "VN");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savings_fragment);

        rateRepository = new RateRepository();

        if (getIntent().hasExtra("TRANSACTION_TYPE")) {
            transactionType = getIntent().getStringExtra("TRANSACTION_TYPE");
        }

        initViews();
        fetchUserAccountInfo();
        setupDynamicUI();
        setupTermButtons();
        setupQuickAmountButtons();
        setupAmountInput();
        setupSubmitButton();

        fetchCommonInterestRate();
    }

    private void initViews() {
        appBar = findViewById(R.id.app_bar);
        navBar = findViewById(R.id.nav_bar);
        btnSubmit = findViewById(R.id.btn_submit_savings);
        etAmount = findViewById(R.id.et_savings_amount);
        btn5M = findViewById(R.id.btn_5m);
        btn10M = findViewById(R.id.btn_10m);
        btn50M = findViewById(R.id.btn_50m);

        tvScreenTitle = findViewById(R.id.tv_screen_title);
        tvTermLabel = findViewById(R.id.tv_term_value);
        tvInterestRateLabel = findViewById(R.id.tv_rate_value);
        tvPrincipal = findViewById(R.id.tv_principal_value);
        tvInterestAmount = findViewById(R.id.tv_interest_value);
        tvTotalAmount = findViewById(R.id.tv_total_value);
        tvNote = findViewById(R.id.tv_note);
        tvTotalLabel = findViewById(R.id.tv_total_label);

        tvLabelInputAmount = findViewById(R.id.tv_label_input_amount);
        tvLabelChooseTerm = findViewById(R.id.tv_label_choose_term);
        layoutMonthlyPayment = findViewById(R.id.layout_monthly_payment);
        tvMonthlyPaymentValue = findViewById(R.id.tv_monthly_payment_value);

        tvSourceAccountNumber = findViewById(R.id.tv_source_account);
        tvSourceBalance = findViewById(R.id.tv_source_balance);

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

    private void fetchUserAccountInfo() {
        Account account = SessionManager.getInstance(this).getAccount();
        if (account == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String username = account.getUsername();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("account")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot accDoc = querySnapshot.getDocuments().get(0);
                        currentAccountId = accDoc.getId();
                        Double balance = accDoc.getDouble("balance");
                        String cardNum = accDoc.getString("cardNumber");

                        sourceAccountBalance = (balance != null) ? balance : 0.0;

                        DecimalFormat df = new DecimalFormat("#,###");
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols(localeVN);
                        symbols.setGroupingSeparator('.');
                        df.setDecimalFormatSymbols(symbols);

                        if (tvSourceBalance != null) {
                            tvSourceBalance.setText(df.format(sourceAccountBalance) + " đ");
                        }

                        if (tvSourceAccountNumber != null && cardNum != null && cardNum.length() > 3) {
                            tvSourceAccountNumber.setText("*******" + cardNum.substring(cardNum.length() - 3));
                        } else if (tvSourceAccountNumber != null) {
                            tvSourceAccountNumber.setText("*******");
                        }
                    } else {
                        Toast.makeText(this, "Không tìm thấy thông tin tài khoản", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void setupDynamicUI() {
        if ("MORTGAGE".equals(transactionType)) {
            if (tvScreenTitle != null) tvScreenTitle.setText("Tài khoản vay vốn");
            btnSubmit.setText("Xác nhận vay vốn");

            if (tvLabelInputAmount != null) tvLabelInputAmount.setText("Số tiền vay");
            if (tvLabelChooseTerm != null) tvLabelChooseTerm.setText("Chọn kỳ hạn vay");

            if (layoutMonthlyPayment != null) layoutMonthlyPayment.setVisibility(View.VISIBLE);
        } else {
            if (tvScreenTitle != null) tvScreenTitle.setText("Tài khoản tiết kiệm");
            btnSubmit.setText("Xác nhận gửi tiết kiệm");

            if (tvLabelInputAmount != null) tvLabelInputAmount.setText("Số tiền gửi");
            if (tvLabelChooseTerm != null) tvLabelChooseTerm.setText("Chọn kỳ hạn gửi");

            if (layoutMonthlyPayment != null) layoutMonthlyPayment.setVisibility(View.GONE);
        }
    }

    private void fetchCommonInterestRate() {
        int termToFetch = 1;

        rateRepository.getRateByTerm(termToFetch, new RateCallback() {
            @Override
            public void onRateLoaded(InterestRate rateObj) {
                if ("MORTGAGE".equals(transactionType)) {
                    baseInterestRate = rateObj.getMortgageRate();
                } else {
                    baseInterestRate = rateObj.getSavingsRate();
                }

                updateAllButtonsUI();

                currentInterestRate = getDynamicInterestRate(1);
                updateCalculations();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(SavingsActivity.this, "Không lấy được lãi suất: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double getDynamicInterestRate(int months) {
        if ("MORTGAGE".equals(transactionType)) {
            return baseInterestRate;
        } else {
            double extraRate = 0.0;
            if (months >= 36) extraRate = 2.5;
            else if (months >= 24) extraRate = 2.0;
            else if (months >= 12) extraRate = 1.5;
            else if (months >= 6) extraRate = 1.0;
            else if (months >= 3) extraRate = 0.5;
            else extraRate = 0.0;

            return baseInterestRate + extraRate;
        }
    }

    private void updateAllButtonsUI() {
        for (MaterialButton btn : termButtons) {
            String originalText = btn.getText().toString();
            int months = 1;
            try {
                String[] parts = originalText.split("\n");
                String termPart = parts[0];
                months = Integer.parseInt(termPart.replace(" THÁNG", "").replace(" tháng", "").trim());
            } catch (Exception e) {
                continue;
            }

            double rateForThisButton = getDynamicInterestRate(months);

            String newText = months + " THÁNG\n" + rateForThisButton + "%/NĂM";
            btn.setText(newText);
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
                if (parts.length > 0) {
                    selectedTermString = parts[0];
                    try {
                        selectedTermMonths = Integer.parseInt(selectedTermString.replace(" THÁNG", "").replace(" tháng", "").trim());
                    } catch (Exception e) {
                        selectedTermMonths = 1;
                    }
                }

                currentInterestRate = getDynamicInterestRate(selectedTermMonths);
                updateCalculations();
            });
        }
    }

    private void updateCalculations() {
        DecimalFormat df = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(localeVN);
        symbols.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(symbols);

        double interest = (currentAmount * currentInterestRate / 100) / 12 * selectedTermMonths;
        double total = currentAmount + interest;

        if (tvTermLabel != null) tvTermLabel.setText(selectedTermMonths + " tháng");
        if (tvInterestRateLabel != null) tvInterestRateLabel.setText(currentInterestRate + "%/năm");
        if (tvPrincipal != null) tvPrincipal.setText(df.format(currentAmount) + " đ");

        if (tvInterestAmount != null) {
            tvInterestAmount.setText("+" + df.format(interest) + " đ");
        }

        if (tvTotalAmount != null) tvTotalAmount.setText(df.format(total) + " đ");

        if (tvTotalLabel != null) {
            if ("MORTGAGE".equals(transactionType)) {
                tvTotalLabel.setText("Tổng gốc và lãi phải trả");
                if (selectedTermMonths > 0) {
                    double monthlyPayment = total / selectedTermMonths;
                    if (tvMonthlyPaymentValue != null) {
                        tvMonthlyPaymentValue.setText(df.format(monthlyPayment) + " đ");
                    }
                }
            } else {
                tvTotalLabel.setText("Tổng tiền nhận được");
            }
        }

        if (tvNote != null) {
            String action = "MORTGAGE".equals(transactionType) ? "phải trả" : "nhận được";
            tvNote.setText("Sau " + selectedTermMonths + " tháng, bạn sẽ " + action + " " + df.format(total) + "đ");
        }
    }

    private void setupAmountInput() {
        if(etAmount == null) return;

        etAmount.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    etAmount.removeTextChangedListener(this);

                    try {
                        String cleanString = s.toString().replaceAll("[,.]", "").trim();

                        if (!cleanString.isEmpty()) {
                            currentAmount = Double.parseDouble(cleanString);

                            DecimalFormat formatter = new DecimalFormat("#,###");
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols(localeVN);
                            symbols.setGroupingSeparator('.');
                            formatter.setDecimalFormatSymbols(symbols);

                            String formatted = formatter.format(currentAmount);

                            current = formatted;
                            etAmount.setText(formatted);
                            etAmount.setSelection(formatted.length());
                        } else {
                            currentAmount = 0;
                            current = "";
                            etAmount.setText("");
                        }
                        updateCalculations();
                    } catch (NumberFormatException e) {
                        currentAmount = 0;
                    }

                    etAmount.addTextChangedListener(this);
                }
            }
        });
    }

    private void setupQuickAmountButtons() {
        View.OnClickListener quickAddListener = v -> {
            Button b = (Button) v;
            String text = b.getText().toString();
            double addAmount = 0;
            if (text.equals("5M")) addAmount = 5000000;
            else if (text.equals("10M")) addAmount = 10000000;
            else if (text.equals("50M")) addAmount = 50000000;

            etAmount.setText(String.format(localeVN, "%.0f", addAmount));
        };
        if(btn5M != null) btn5M.setOnClickListener(quickAddListener);
        if(btn10M != null) btn10M.setOnClickListener(quickAddListener);
        if(btn50M != null) btn50M.setOnClickListener(quickAddListener);
    }

    private void setupSubmitButton() {
        btnSubmit.setOnClickListener(v -> {
            if (currentAccountId == null) {
                Toast.makeText(this, "Đang tải dữ liệu tài khoản, vui lòng đợi...", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentAmount <= 0) {
                Toast.makeText(this, "Số tiền giao dịch phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentAmount < 1000000) {
                Toast.makeText(this, "Số tiền tối thiểu là 1.000.000đ", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("SAVINGS".equals(transactionType)) {
                if (currentAmount > sourceAccountBalance) {
                    Toast.makeText(this, "Số dư tài khoản không đủ!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Intent intent = new Intent(SavingsActivity.this, SavingsConfirmActivity.class);
            intent.putExtra("TRANSACTION_TYPE", transactionType);
            intent.putExtra("AMOUNT", currentAmount);
            intent.putExtra("TERM", selectedTermMonths + " tháng");
            intent.putExtra("RATE", currentInterestRate);
            intent.putExtra("ACCOUNT_ID", currentAccountId);
            startActivity(intent);
        });
    }
}