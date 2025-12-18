package com.example.app.ui.customeview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.example.app.R;
import com.example.app.ui.homecustomer.HomeCustomerActivity;
import com.example.app.ui.homeofficer.HomeOfficerActivity;

public class AppBarOfficerView extends FrameLayout {
    private TextView appBar;

    public AppBarOfficerView(Context context) {
        super(context);
        init(null);
    }


    public AppBarOfficerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AppBarOfficerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.app_bar, this, true);

        appBar = findViewById(R.id.appBar);

        appBar.setOnClickListener(v -> {
            Intent intentHomeOfficer = new Intent(getContext(), HomeOfficerActivity.class);
            getContext().startActivity(intentHomeOfficer);
        });
    }
}
