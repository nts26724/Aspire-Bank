package com.example.app.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.data.model.Account;
import com.example.app.ui.customeview.AppBarView;
import com.example.app.ui.customeview.NavBarView;
import com.example.app.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class TransferActivity extends AppCompatActivity {

    private AppBarView appBar;
    private NavBarView navBar;

    private MaterialButtonToggleGroup toggleGroup;
    private MaterialButton btnTabInternal, btnTabExternal;

    private TextView tvTitle;
    private LinearLayout layoutExternalBank;
    private AutoCompleteTextView actvBankSelector;
    private TextInputEditText etReceiverAccount;
    private TextView tvReceiverName;
    private TextInputEditText etAmount;
    private TextInputEditText etMessage;
    private MaterialButton btnSubmit;

    private TransferViewModel transferViewModel;
    private double currentBalance = 0.0;
    private double transferAmount = 0.0;

    private String currentUserPhone = null;
    private String currentSenderName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_activity);

        initViews();
        setupObservers();
        setupUserInfo();
        setupTabs();
        setupBankDropdown();
        setupAmountInput();
        setupListeners();
    }

    private void initViews() {
        appBar = findViewById(R.id.app_bar);
        navBar = findViewById(R.id.nav_bar);

        toggleGroup = findViewById(R.id.toggle_button_group);
        btnTabInternal = findViewById(R.id.btn_tab_internal);
        btnTabExternal = findViewById(R.id.btn_tab_external);

        tvTitle = findViewById(R.id.tv_transfer_title);

        layoutExternalBank = findViewById(R.id.layout_external_bank);
        actvBankSelector = findViewById(R.id.actv_bank_selector);

        etReceiverAccount = findViewById(R.id.et_receiver_account);
        tvReceiverName = findViewById(R.id.tv_receiver_name);

        etAmount = findViewById(R.id.et_transfer_amount);
        etMessage = findViewById(R.id.et_transfer_message);
        btnSubmit = findViewById(R.id.btn_submit_transfer);

        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);
    }

    private void setupObservers() {
        transferViewModel.getReceiverNameLiveData().observe(this, name -> {
            if (name != null) {
                tvReceiverName.setText(name);
            }
        });

        transferViewModel.getErrorMessageLiveData().observe(this, error -> {
            if (error != null) {
                tvReceiverName.setText(error);
            }
        });

        transferViewModel.getCurrentBalanceLiveData().observe(this, balance -> {
            if (balance != null) {
                currentBalance = balance;
            }
        });
    }

    private void setupUserInfo() {
        Account account = SessionManager.getInstance(this).getAccount();
        if (account == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        transferViewModel.fetchCurrentBalance(account.getUsername());

        FirebaseFirestore.getInstance().collection("customer")
                .whereEqualTo("username", account.getUsername())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        currentUserPhone = doc.getString("phoneNumber");
                        currentSenderName = doc.getString("fullName");
                    }
                });
    }

    private void setupTabs() {
        if (toggleGroup.getCheckedButtonId() == View.NO_ID) {
            toggleGroup.check(R.id.btn_tab_internal);
        }

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                etReceiverAccount.setText("");
                tvReceiverName.setText("");
                etAmount.setText("");
                etMessage.setText("");

                if (checkedId == R.id.btn_tab_internal) {
                    tvTitle.setText("Chuyển tiền nội bộ");
                    layoutExternalBank.setVisibility(View.GONE);
                } else if (checkedId == R.id.btn_tab_external) {
                    tvTitle.setText("Chuyển tiền liên ngân hàng");
                    layoutExternalBank.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setupBankDropdown() {
        String[] banks = {"Vietcombank", "Techcombank", "MB Bank", "ACB", "VPBank", "BIDV", "VietinBank"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, banks);
        actvBankSelector.setAdapter(adapter);

        actvBankSelector.setOnClickListener(v -> actvBankSelector.showDropDown());
        actvBankSelector.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) actvBankSelector.showDropDown();
        });
        actvBankSelector.setShowSoftInputOnFocus(false);
    }

    private void setupAmountInput() {
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                etAmount.removeTextChangedListener(this);
                try {
                    String originalString = s.toString().replaceAll("[,.]", "");
                    if (!originalString.isEmpty()) {
                        double parsed = Double.parseDouble(originalString);
                        transferAmount = parsed;
                        DecimalFormat formatter = new DecimalFormat("#,###");
                        etAmount.setText(formatter.format(parsed));
                        etAmount.setSelection(etAmount.getText().length());
                    } else {
                        transferAmount = 0;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                etAmount.addTextChangedListener(this);
            }
        });
    }

    private void setupListeners() {
        etReceiverAccount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String accNum = etReceiverAccount.getText().toString().trim();
                boolean isInternal = (toggleGroup.getCheckedButtonId() == R.id.btn_tab_internal);
                String bankName = actvBankSelector.getText().toString();

                if (!accNum.isEmpty()) {
                    tvReceiverName.setText("Đang kiểm tra...");
                    transferViewModel.findReceiverByCardNumber(accNum, isInternal, bankName);
                }
            }
        });

        btnSubmit.setOnClickListener(v -> validateAndSubmit());
    }

    private void validateAndSubmit() {
        String receiverAcc = etReceiverAccount.getText().toString().trim();
        String message = etMessage.getText().toString().trim();
        boolean isExternal = (toggleGroup.getCheckedButtonId() == R.id.btn_tab_external);

        if (isExternal && TextUtils.isEmpty(actvBankSelector.getText().toString())) {
            Toast.makeText(this, "Vui lòng chọn ngân hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(receiverAcc)) {
            Toast.makeText(this, "Vui lòng nhập số tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        if (transferAmount < 10000) {
            Toast.makeText(this, "Số tiền tối thiểu là 10.000đ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (transferAmount > currentBalance) {
            Toast.makeText(this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUserPhone == null || currentSenderName == null) {
            Toast.makeText(this, "Đang tải thông tin, vui lòng đợi...", Toast.LENGTH_SHORT).show();
            setupUserInfo();
            return;
        }

        Intent intent = new Intent(TransferActivity.this, TransferVerifyOTP.class);

        intent.putExtra("TRANSACTION_TYPE", isExternal ? "TRANSFER_EXTERNAL" : "TRANSFER_INTERNAL");
        intent.putExtra("AMOUNT", transferAmount);
        intent.putExtra("MESSAGE", message);
        intent.putExtra("RECEIVER_ACCOUNT", receiverAcc);
        intent.putExtra("PHONE_NUMBER", currentUserPhone);
        intent.putExtra("SENDER_NAME", currentSenderName);

        if (isExternal) {
            intent.putExtra("BANK_NAME", actvBankSelector.getText().toString());
        }

        String receiverName = tvReceiverName.getText().toString();
        if (!receiverName.isEmpty() && !receiverName.contains("...")) {
            intent.putExtra("RECEIVER_NAME", receiverName);
        } else {
            intent.putExtra("RECEIVER_NAME", "Người nhận");
        }

        startActivity(intent);
    }
}