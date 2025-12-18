package com.example.app.ui.listcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.adapter.ListCustomerAdapter;
import com.google.android.material.search.SearchBar;

public class ListCustomerActivity extends AppCompatActivity {
    private ImageView add;
    private RecyclerView listCustomer;
    private SearchBar search;
    private ListCustomerViewModel listCustomerViewModel;
    private CardView emptyNotification;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.list_customer_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list_customer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        listCustomerViewModel.getListCustomer();
        listCustomerViewModel.getListCustomerLiveData().observe(
            this, listCustomerLiveData -> {
                if(listCustomerLiveData == null || listCustomerLiveData.size() == 0) {
                    listCustomer.setVisibility(View.GONE);
                    emptyNotification.setVisibility(View.VISIBLE);
                    Log.d("listCustomerLiveData", "listCustomerLiveData: 0");
                    return;
                }

                emptyNotification.setVisibility(View.GONE);
                listCustomer.setVisibility(View.VISIBLE);
                listCustomer.setAdapter(
                        new ListCustomerAdapter(listCustomerLiveData));
                Log.d("listCustomerLiveData", "listCustomerLiveData: >0");
            }
        );

        add.setOnClickListener(v -> {
            Intent intentListCustomerDetail = new Intent(this, ListCustomerDetail.class);
            startActivity(intentListCustomerDetail);
        });
    }

    public void init() {
        add = findViewById(R.id.add);
        listCustomer = findViewById(R.id.listCustomer);
        search = findViewById(R.id.search);
        emptyNotification = findViewById(R.id.emptyNotification);

        listCustomerViewModel = new ViewModelProvider(this).get(ListCustomerViewModel.class);
    }
}
