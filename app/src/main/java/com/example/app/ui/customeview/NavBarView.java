package com.example.app.ui.customeview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.app.R;
import com.example.app.ui.bookroom.BookRoomMap;
import com.example.app.ui.homecustomer.HomeCustomerActivity;
import com.example.app.ui.notification.NotificationActivity;
import com.example.app.ui.receiptpayment.ReceiptPaymentActivity;

public class NavBarView extends FrameLayout {
    ImageView homeCustomer, notification, utility, map, profile;

    public NavBarView(Context context) {
        super(context);
        init(null);
    }


    public NavBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NavBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.nav_bar, this, true);

        homeCustomer = findViewById(R.id.homeCustomer);
        notification = findViewById(R.id.notification);
        utility = findViewById(R.id.utility);
        map = findViewById(R.id.map);
        profile = findViewById(R.id.profile);


        homeCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HomeCustomerActivity.class);
            getContext().startActivity(intent);
        });


        notification.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NotificationActivity.class);
            getContext().startActivity(intent);
        });


        utility.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ReceiptPaymentActivity.class);
            getContext().startActivity(intent);
        });


        map.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), BookRoomMap.class);
            getContext().startActivity(intent);
        });


        profile.setOnClickListener(v -> {
//            Intent intent = new Intent(getContext(), ProfileActivity.class);
//            getContext().startActivity(intent);
        });
    }
}
