package com.example.app.ui.bookroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.example.app.ui.verify.VerifyOTPActivity;
import com.example.app.utils.SessionManager;

public class BookRoomVerifyOTP extends VerifyOTPActivity {
    public void customeAction() {
        String nameCustomerStr = intentSource.getStringExtra("username");
        String hotelNameStr = intentSource.getStringExtra("hotelName");
        String quantityPeopleStr = intentSource.getStringExtra("quantityPeople");
        String quantityRoomStr = intentSource.getStringExtra("quantityRoom");
        String checkInDateStr = intentSource.getStringExtra("checkInDate");
        String checkOutDateStr = intentSource.getStringExtra("checkOutDate");
        String priceStr = intentSource.getStringExtra("price");

        verifyOTPViewModel.widthraw(
                SessionManager.getInstance(this).getAccount().getUsername(),
                Long.parseLong(priceStr.replaceAll("\\.", "")),
                "BookRoom Payment", hotelNameStr);

        verifyOTPViewModel.getBooleanLiveData().observe(this, isSuccessful -> {
            if(isSuccessful){
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
    }
}
