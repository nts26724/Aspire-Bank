package com.example.app.ui.customeview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.example.app.R;
import com.example.app.ui.bookroom.BookRoomActivity;
import com.example.app.ui.bookticket.BookTicketActivity;
import com.example.app.ui.depositphone.DepositPhoneActivity;
import com.example.app.ui.receiptpayment.ReceiptPaymentActivity;

public class UtilityBarView extends FrameLayout {
    TextView receiptPayment, depositPhone, bookTicket, bookRoom;

    public UtilityBarView(Context context) {
        super(context);
        init(null);
    }

    public UtilityBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UtilityBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.utility_bar, this, true);

        receiptPayment = findViewById(R.id.receiptPayment);
        depositPhone = findViewById(R.id.depositPhone);
        bookTicket = findViewById(R.id.bookTicket);
        bookRoom = findViewById(R.id.bookRoom);


        receiptPayment.setOnClickListener(v -> {
            setColorReceiptPayment();

            Intent intent = new Intent(getContext(), ReceiptPaymentActivity.class);
            getContext().startActivity(intent);
        });


        depositPhone.setOnClickListener(v -> {
            setColorDepositPhone();

            Intent intent = new Intent(getContext(), DepositPhoneActivity.class);
            getContext().startActivity(intent);
        });


        bookTicket.setOnClickListener(v -> {
            setColorBookTicket();

            Intent intent = new Intent(getContext(), BookTicketActivity.class);
            getContext().startActivity(intent);
        });


        bookRoom.setOnClickListener(v -> {
            setColorBookRoom();

            Intent intent = new Intent(getContext(), BookRoomActivity.class);
            getContext().startActivity(intent);
        });
    }


    public void setColorReceiptPayment() {
        receiptPayment.setBackgroundColor(Color.parseColor("#FEDDBC"));
        depositPhone.setBackgroundColor(Color.WHITE);
        bookTicket.setBackgroundColor(Color.WHITE);
        bookRoom.setBackgroundColor(Color.WHITE);
    }

    public void setColorDepositPhone() {
        receiptPayment.setBackgroundColor(Color.WHITE);
        depositPhone.setBackgroundColor(Color.parseColor("#FEDDBC"));
        bookTicket.setBackgroundColor(Color.WHITE);
        bookRoom.setBackgroundColor(Color.WHITE);
    }

    public void setColorBookTicket() {
        receiptPayment.setBackgroundColor(Color.WHITE);
        depositPhone.setBackgroundColor(Color.WHITE);
        bookTicket.setBackgroundColor(Color.parseColor("#FEDDBC"));
        bookRoom.setBackgroundColor(Color.WHITE);
    }

    public void setColorBookRoom() {
        receiptPayment.setBackgroundColor(Color.WHITE);
        depositPhone.setBackgroundColor(Color.WHITE);
        bookTicket.setBackgroundColor(Color.WHITE);
        bookRoom.setBackgroundColor(Color.parseColor("#FEDDBC"));
    }
}
