package com.example.app.ui.depositphone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.example.app.ui.customeview.UtilityBarView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class DepositPhoneActivity extends AppCompatActivity {
    private MaterialButton btn10, btn20, btn50, btn100, btn200, btn500;
    private TextInputEditText phoneNumber;
    private TextView btnDepositPhone;
    private MaterialButton selectedButton = null;
    private UtilityBarView utilityBarView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.deposit_phone_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.deposit_phone), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        utilityBarView.setColorDepositPhone();

        btn10.setOnClickListener(v -> {
            selectButton(btn10);
        });

        btn20.setOnClickListener(v -> {
            selectButton(btn20);
        });

        btn50.setOnClickListener(v -> {
            selectButton(btn50);
        });

        btn100.setOnClickListener(v -> {
            selectButton(btn100);
        });

        btn200.setOnClickListener(v -> {
            selectButton(btn200);
        });

        btn500.setOnClickListener(v -> {
            selectButton(btn500);
        });

        btnDepositPhone.setOnClickListener(v -> {
            String phoneNumberStr = phoneNumber.getText().toString().trim();
            if (phoneNumberStr.isEmpty()) {
                Toast.makeText(this, "Hãy nhập số điện thoại", Toast.LENGTH_SHORT).show();
            } else if(phoneNumberStr.charAt(0) != '0' || phoneNumberStr.length() != 10) {
                Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            } else {
                Intent intentDepositPhoneVerifyOTP = new Intent(DepositPhoneActivity.this, DepositPhoneVerifyOTP.class);
                intentDepositPhoneVerifyOTP.putExtra("phoneNumber", phoneNumberStr);
                intentDepositPhoneVerifyOTP.putExtra(
                        "amount",
                        selectedButton.getText().toString().split(" ")[0]
                                .replaceAll("\\.", "")
                );
                startActivity(intentDepositPhoneVerifyOTP);
            }
        });
    }

    public void init() {
        btn10 = findViewById(R.id.btn10);
        btn20 = findViewById(R.id.btn20);
        btn50 = findViewById(R.id.btn50);
        btn100 = findViewById(R.id.btn100);
        btn200 = findViewById(R.id.btn200);
        btn500 = findViewById(R.id.btn500);

        phoneNumber = findViewById(R.id.phoneNumber);
        btnDepositPhone = findViewById(R.id.btnDepositPhone);

        utilityBarView = findViewById(R.id.utilityBarView);
    }

    private void selectButton(MaterialButton button) {
        if (selectedButton != null) {
            selectedButton.setChecked(false);
            selectedButton.setBackgroundColor(Color.parseColor("#fff1d6"));
        }

        button.setChecked(true);
        button.setBackgroundColor(Color.parseColor("#ff8d6d"));
        selectedButton = button;
    }
}
