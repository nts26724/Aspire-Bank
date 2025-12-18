package com.example.app.ui.customeview;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.ui.homeofficer.HomeOfficerActivity;
import com.example.app.ui.listcustomer.ListCustomerActivity;
import com.example.app.ui.profileofficer.ProfileOfficerActivity;
import com.example.app.ui.rate.RateActivity;

public class NavBarOfficerView extends FrameLayout {
    private ImageView homeOfficer, lstCustomer, rate, profile;

    public NavBarOfficerView(Context context) {
        super(context);
        init(null);
    }


    public NavBarOfficerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NavBarOfficerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.nav_bar_officer, this, true);

        homeOfficer = findViewById(R.id.homeOfficer);
        lstCustomer = findViewById(R.id.lstCustomer);
        rate = findViewById(R.id.rate);
        profile = findViewById(R.id.profile);

        homeOfficer.setOnClickListener(v -> {
            Intent intentHomeOfficer = new Intent(getContext(), HomeOfficerActivity.class);
            getContext().startActivity(intentHomeOfficer);
        });

        lstCustomer.setOnClickListener(v -> {
            Intent intentLstCustomer = new Intent(getContext(), ListCustomerActivity.class);
            getContext().startActivity(intentLstCustomer);
        });

        rate.setOnClickListener(v -> {
            Intent intentRate = new Intent(getContext(), RateActivity.class);
            getContext().startActivity(intentRate);
        });

        profile.setOnClickListener(v -> {
            Intent intentProfileOfficer = new Intent(getContext(), ProfileOfficerActivity.class);
            getContext().startActivity(intentProfileOfficer);
        });
    }
}
