package com.example.app.ui.bookroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.example.app.ui.homecustomer.HomeCustomerActivity;

public class BookRoomSuccess extends AppCompatActivity {
    private TextView username, hotelName,
            quantityPeople, quantityRoom,
            checkInDate, checkOutDate, price,
            continueBookRoom, homeCustomer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_room_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.book_room_success), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("BookRoomSuccess", "onCreate: ");
        init();

        Intent intentSource = getIntent();

        String usernameStr = intentSource.getStringExtra("username");
        String hotelNameStr = intentSource.getStringExtra("hotelName");
        String quantityPeopleStr = intentSource.getStringExtra("quantityPeople");
        String quantityRoomStr = intentSource.getStringExtra("quantityRoom");
        String checkInDateStr = intentSource.getStringExtra("checkInDate");
        String checkOutDateStr = intentSource.getStringExtra("checkOutDate");
        String priceStr = intentSource.getStringExtra("price");


        username.setText(usernameStr);
        hotelName.setText(hotelNameStr);
        quantityPeople.setText(quantityPeopleStr);
        quantityRoom.setText(quantityRoomStr);
        checkInDate.setText(checkInDateStr);
        checkOutDate.setText(checkOutDateStr);
        price.setText(priceStr + " VND");


        continueBookRoom.setOnClickListener(v -> {
            Intent intentBookRoomList = new Intent(this, BookRoomActivity.class);
            startActivity(intentBookRoomList);
        });


        homeCustomer.setOnClickListener(v -> {
            Intent intentHomeCustomer = new Intent(this, HomeCustomerActivity.class);
            startActivity(intentHomeCustomer);
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

        continueBookRoom = findViewById(R.id.continueBookRoom);
        homeCustomer = findViewById(R.id.homeCustomer);
    }
}
