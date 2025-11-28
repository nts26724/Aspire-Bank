package com.example.app.ui.depositphone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;

public class DepositPhoneVerify extends AppCompatActivity {
    TextView reSendOTP;
    Button confirm, cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.verify);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.verify), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String amount = intent.getStringExtra("amount");

        confirm.setOnClickListener(v -> {
            // check
            // if(false)
                Toast.makeText(this, "Sai mã OTP", Toast.LENGTH_SHORT).show();
            // if(true)
                Intent intentDepositPhoneSuccess = new Intent(this, DepositPhoneSuccess.class);
                intentDepositPhoneSuccess.putExtra("phoneNumber", phoneNumber);
                intentDepositPhoneSuccess.putExtra("amount", amount);
                startActivity(intentDepositPhoneSuccess);
        });

        cancel.setOnClickListener(v -> {
            finish();
        });

        reSendOTP.setOnClickListener(v -> {

        });
    }

    public void init() {
        reSendOTP = findViewById(R.id.reSendOTP);

        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
    }
}
