package com.example.app.ui.customeview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.example.app.R;
import com.example.app.ui.homecustomer.HomeCustomerActivity;

public class AppBarView extends FrameLayout {
    TextView textView;

    public AppBarView(Context context) {
        super(context);
        init(null);
    }


    public AppBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AppBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.app_bar, this, true);

        textView = findViewById(R.id.appBar);

        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HomeCustomerActivity.class);
            getContext().startActivity(intent);
        });
    }
}
