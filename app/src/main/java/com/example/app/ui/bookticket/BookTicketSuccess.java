package com.example.app.ui.bookticket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.example.app.ui.homecustomer.HomeCustomerActivity;

public class BookTicketSuccess extends AppCompatActivity {
    private TextView nameCustomer, nameAirline,
            origin, destination,
            departureArriveTime, departureDate,
            price, continueBookTicket, homeCustomer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_ticket_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.book_ticket_success), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Intent intentSource = getIntent();

        String nameCustomerStr = intentSource.getStringExtra("nameCustomer");
        String nameAirlineStr = intentSource.getStringExtra("nameAirline");
        String originStr = intentSource.getStringExtra("origin");
        String destinationStr = intentSource.getStringExtra("destination");
        String departureArriveTimeStr = intentSource.getStringExtra("departureArriveTime");
        String departureDateStr = intentSource.getStringExtra("departureDate");
        String priceStr = intentSource.getStringExtra("price");

        continueBookTicket.setOnClickListener(v -> {
            Intent intentBookTicket = new Intent(this, BookTicketActivity.class);
            startActivity(intentBookTicket);
        });

        homeCustomer.setOnClickListener(v -> {
            Intent intentHomeCustomer = new Intent(this, HomeCustomerActivity.class);
            startActivity(intentHomeCustomer);
        });
    }

    public void init() {
        nameCustomer = findViewById(R.id.nameCutomer);
        nameAirline = findViewById(R.id.nameAirline);
        origin = findViewById(R.id.origin);
        destination = findViewById(R.id.destination);
        departureArriveTime = findViewById(R.id.departureArriveTime);
        departureDate = findViewById(R.id.departureDate);
        price = findViewById(R.id.price);

        continueBookTicket = findViewById(R.id.continueBookTicket);
        homeCustomer = findViewById(R.id.homeCustomer);
    }
}
