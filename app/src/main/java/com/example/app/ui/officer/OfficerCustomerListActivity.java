package com.example.app.ui.officer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.app.R;

public class OfficerCustomerListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView btnMenu;
    private LinearLayout menuDashboard, menuCustomer, menuInterest, menuEkyc, menuProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.officer_customer_list);

        initViews();
        setupListeners();
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        btnMenu = findViewById(R.id.btn_menu);

        menuDashboard = findViewById(R.id.menu_dashboard);
        menuCustomer = findViewById(R.id.menu_customer);
        menuInterest = findViewById(R.id.menu_interest);
        menuEkyc = findViewById(R.id.menu_ekyc);
        menuProfile = findViewById(R.id.menu_profile);
    }

    private void setupListeners() {
        btnMenu.setOnClickListener(v -> {
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        if (menuDashboard != null) {
            menuDashboard.setOnClickListener(v -> {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(OfficerCustomerListActivity.this, OfficerDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        if (menuCustomer != null) {
            menuCustomer.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));
        }

        View.OnClickListener developingListener = v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        };

        if (menuInterest != null) menuInterest.setOnClickListener(developingListener);
        if (menuEkyc != null) menuEkyc.setOnClickListener(developingListener);
        if (menuProfile != null) menuProfile.setOnClickListener(developingListener);
    }
}