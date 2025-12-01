package com.example.app.ui.notification;

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
import com.example.app.adapter.NotificationAdapter;
import com.example.app.ui.homecustomer.HomeCustomerViewModel;
import com.example.app.utils.SessionManager;

import java.util.Collection;
import java.util.Collections;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView lstNotification;
    private NotificationViewModel notificationViewModel;
    private TextView textNotification;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.notification_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.notification), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        notificationViewModel.getTransactionsByUsername(SessionManager.getInstance(this).getAccount().getUsername());

        notificationViewModel.getListNotification().observe(this, transactions -> {
            if (transactions == null) {
                lstNotification.setVisibility(View.GONE);
                textNotification.setVisibility(View.VISIBLE);
                return;
            }

            lstNotification.setVisibility(View.VISIBLE);
            textNotification.setVisibility(View.GONE);
            Collections.sort(transactions, (t1, t2) ->
                            t2.getTimestamp() < t1.getTimestamp() ? 1 : -1);
            lstNotification.setAdapter(new NotificationAdapter(transactions));
        });


    }

    public void init() {
        lstNotification = findViewById(R.id.lstNotification);
        textNotification = findViewById(R.id.textNotification);



        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
    }
}
