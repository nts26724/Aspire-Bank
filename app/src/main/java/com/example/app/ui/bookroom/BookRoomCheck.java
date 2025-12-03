package com.example.app.ui.bookroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.data.model.Account;
import com.example.app.utils.SessionManager;

public class BookRoomCheck extends AppCompatActivity {
    TextView username, hotelName, quantityPeople, quantityRoom,
            checkInDate, checkOutDate, price, payment, cancel;
    BookRoomViewModel bookRoomViewModel;
    Account account;
    private String nameCustomerStr;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_room_check);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.book_room_check), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        bookRoomViewModel.getNameCustomerByUsername(
                SessionManager.getInstance(this).getAccount().getUsername()
        );

        bookRoomViewModel.getNameCustomerLiveData().observe(this, nameCustomer -> {
            nameCustomerStr = nameCustomer;
            username.setText(nameCustomerStr);
        });

        Intent intentSource = getIntent();

        String offerId = intentSource.getStringExtra("offerID");
        String hotelNameStr = intentSource.getStringExtra("name");
        String quantityPeopleStr = intentSource.getStringExtra("quantityPeople");
        String quantityRoomStr = intentSource.getStringExtra("quantityRoom");
        String checkInDateStr = intentSource.getStringExtra("checkInDate");
        String checkOutDateStr = intentSource.getStringExtra("checkOutDate");
        String priceStr = intentSource.getStringExtra("price");


        hotelName.setText(hotelNameStr);
        quantityPeople.setText(quantityPeopleStr);
        quantityRoom.setText(quantityRoomStr);
        checkInDate.setText(checkInDateStr);
        checkOutDate.setText(checkOutDateStr);
        price.setText(priceStr + " VND");

        payment.setOnClickListener(v -> {
            bookRoomViewModel.pay(
                    hotelNameStr,
                    offerId,
                    Long.parseLong(priceStr.replaceAll("\\.", ""))
            );

            bookRoomViewModel.getBookingLiveData().observe(this, isSuccessful -> {
                Log.d("obserBookingLiveData", "tren if");
                if(isSuccessful) {
                    Log.d("obserBookingLiveData", "if true");
                    Intent intentBookRoomSuccess = new Intent(this, BookRoomSuccess.class);
                    intentBookRoomSuccess.putExtra("username", nameCustomerStr);
                    intentBookRoomSuccess.putExtra("hotelName", hotelNameStr);
                    intentBookRoomSuccess.putExtra("quantityPeople", quantityPeopleStr);
                    intentBookRoomSuccess.putExtra("quantityRoom", quantityRoomStr);
                    intentBookRoomSuccess.putExtra("checkInDate", checkInDateStr);
                    intentBookRoomSuccess.putExtra("checkOutDate", checkOutDateStr);
                    intentBookRoomSuccess.putExtra("price", priceStr);
                    startActivity(intentBookRoomSuccess);
                } else {
                    Log.d("obserBookingLiveData", "if false");
                    Toast.makeText(this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
                }
            });
        });

        cancel.setOnClickListener(v -> {
            finish();
        });
    }

    public void init() {
        username = findViewById(R.id.username);
        hotelName = findViewById(R.id.hotelName);
        quantityPeople = findViewById(R.id.quantityPeople);
        quantityRoom = findViewById(R.id.quantityRoom);
        checkInDate = findViewById(R.id.checkInDate);
        checkOutDate = findViewById(R.id.checkOutDate);
        price = findViewById(R.id.price);
        payment = findViewById(R.id.payment);
        cancel = findViewById(R.id.cancel);

        bookRoomViewModel = new ViewModelProvider(this).get(BookRoomViewModel.class);
    }
}
