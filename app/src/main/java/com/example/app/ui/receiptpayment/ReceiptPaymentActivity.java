package com.example.app.ui.receiptpayment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.adapter.ReceiptPaymentAdapter;
import com.example.app.utils.SessionManager;

public class ReceiptPaymentActivity extends AppCompatActivity {
    private RecyclerView lstReceipt;
    private TextView textReceipt;
    private ReceiptPaymentViewModel receiptPaymentViewModel;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.receipt_payment_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.receipt_payment), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        receiptPaymentViewModel.getReceiptsByUsername(SessionManager.getInstance(this).getAccount().getUsername());

        receiptPaymentViewModel.getListReceipts().observe(this, receipts -> {
            if(receipts == null || receipts.isEmpty()) {
                lstReceipt.setVisibility(View.GONE);
                textReceipt.setVisibility(View.VISIBLE);
                return;
            }

            lstReceipt.setVisibility(View.VISIBLE);
            textReceipt.setVisibility(View.GONE);
            lstReceipt.setAdapter(new ReceiptPaymentAdapter(receipts));
        });
    }

    public void init() {
        lstReceipt = findViewById(R.id.lstReceipt);
        textReceipt = findViewById(R.id.textReceipt);

        receiptPaymentViewModel = new ViewModelProvider(this).get(ReceiptPaymentViewModel.class);
    }
}
