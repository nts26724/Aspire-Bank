package com.example.app.ui.bookticket;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.app.ui.verify.VerifyOTPActivity;
import com.example.app.utils.SessionManager;

public class BookTicketVerifyOTP extends VerifyOTPActivity {
    public void customeAction() {
        String nameCustomerStr = intentSource.getStringExtra("nameCustomer");
        String nameAirlineStr = intentSource.getStringExtra("nameAirline");
        String originStr = intentSource.getStringExtra("origin");
        String destinationStr = intentSource.getStringExtra("destination");
        String departureArriveTimeStr = intentSource.getStringExtra("departureArriveTime");
        String departureDateStr = intentSource.getStringExtra("departureDate");
        String priceStr = intentSource.getStringExtra("price");

        verifyOTPViewModel.widthraw(
            SessionManager.getInstance(this).getAccount().getUsername(),
            Long.parseLong(priceStr.replaceAll("\\.", "")),
            "BookTicket Payment",
            nameAirlineStr
        );


        verifyOTPViewModel.getBooleanLiveData().observe(this, isSuccessful -> {
            if(isSuccessful) {
                Intent intentBookTicketSuccess = new Intent(this, BookTicketSuccess.class);
                intentBookTicketSuccess.putExtra("nameCustomer", nameCustomerStr);
                intentBookTicketSuccess.putExtra("nameAirline", nameAirlineStr);
                intentBookTicketSuccess.putExtra("origin", originStr);
                intentBookTicketSuccess.putExtra("destination", destinationStr);
                intentBookTicketSuccess.putExtra("departureArriveTime", departureArriveTimeStr);
                intentBookTicketSuccess.putExtra("departureDate", departureDateStr);
                intentBookTicketSuccess.putExtra("price", priceStr);
                startActivity(intentBookTicketSuccess);
            } else {
                Log.d("obserBookingLiveData", "if false");
                Toast.makeText(this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
