package com.example.app.ui.bookticket;

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
import com.example.app.utils.SessionManager;

public class BookTicketCheck extends AppCompatActivity {
    private TextView nameCustomer, nameAirline,
            origin, destination,
            departureArriveTime, departureDate,
            price, pay, cancel;

    private BookTicketViewModel bookTicketViewModel;
     private String nameCustomerStr;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_ticket_check);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.book_ticket_check), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();


        bookTicketViewModel.getNameCustomerByUsername(
                SessionManager.getInstance(this).getAccount().getUsername()
        );

        bookTicketViewModel.getNameCustomerLiveData().observe(this, nameCustomerLiveData -> {
            nameCustomerStr = nameCustomerLiveData;
            nameCustomer.setText(nameCustomerStr);
        });


        Intent intentSource = getIntent();

        String nameAirlineStr = intentSource.getStringExtra("nameAirline");
        String originStr = intentSource.getStringExtra("origin");
        String destinationStr = intentSource.getStringExtra("destination");
        String departureArriveTimeStr = intentSource.getStringExtra("departureArriveTime");
        String departureDateStr = intentSource.getStringExtra("departureDate");
        String priceStr = intentSource.getStringExtra("price");

        nameAirline.setText(nameAirlineStr);
        origin.setText(originStr);
        destination.setText(destinationStr);
        departureArriveTime.setText(departureArriveTimeStr);
        departureDate.setText(departureDateStr);
        price.setText(priceStr + " VND");


        pay.setOnClickListener(v -> {
            Intent intentBookTicketVerifyOTP = new Intent(this, BookTicketVerifyOTP.class);
            intentBookTicketVerifyOTP.putExtra("nameCustomer", nameCustomerStr);
            intentBookTicketVerifyOTP.putExtra("nameAirline", nameAirlineStr);
            intentBookTicketVerifyOTP.putExtra("origin", originStr);
            intentBookTicketVerifyOTP.putExtra("destination", destinationStr);
            intentBookTicketVerifyOTP.putExtra("departureArriveTime", departureArriveTimeStr);
            intentBookTicketVerifyOTP.putExtra("departureDate", departureDateStr);
            intentBookTicketVerifyOTP.putExtra("price", priceStr);
            startActivity(intentBookTicketVerifyOTP);
        });

        cancel.setOnClickListener(v -> {
            finish();
        });
    }

    public void init() {
        nameCustomer = findViewById(R.id.nameCustomer);
        nameAirline = findViewById(R.id.nameAirline);
        origin = findViewById(R.id.origin);
        destination = findViewById(R.id.destination);
        departureArriveTime = findViewById(R.id.departureArriveTime);
        departureDate = findViewById(R.id.departureDate);
        price = findViewById(R.id.price);

        pay = findViewById(R.id.pay);
        cancel = findViewById(R.id.cancel);

        bookTicketViewModel = new ViewModelProvider(this).get(BookTicketViewModel.class);
    }
}
