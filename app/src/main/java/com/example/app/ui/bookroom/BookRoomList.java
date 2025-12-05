package com.example.app.ui.bookroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.adapter.BookRoomAdapter;
import com.example.app.ui.customeview.UtilityBarView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Collections;

public class BookRoomList extends AppCompatActivity {
    private RecyclerView listRoom;
    private BookRoomViewModel bookRoomViewModel;
    private TextView textNotification;
    UtilityBarView utilityBarView;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_room_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.book_room_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        utilityBarView.setColorBookRoom();


        Intent intentSource = getIntent();

        bookRoomViewModel.getListHotelOffer(
                intentSource.getStringExtra("latitude"),
                intentSource.getStringExtra("longitude"),
                intentSource.getStringExtra("radius"),
                intentSource.getStringExtra("quantityPeople"),
                intentSource.getStringExtra("checkInDate"),
                intentSource.getStringExtra("checkOutDate"),
                intentSource.getStringExtra("quantityRoom")
        );

        bookRoomViewModel.getListHotelOfferLiveData().observe(this,
                listHotelOffer -> {


            if(listHotelOffer == null || listHotelOffer.isEmpty()) {
                listRoom.setVisibility(View.GONE);
                textNotification.setVisibility(View.VISIBLE);
                Log.d("load data", "listHotelOffer == null");
            } else {
                listRoom.setVisibility(View.VISIBLE);
                textNotification.setVisibility(View.GONE);
                Collections.sort(listHotelOffer, (ho1, ho2) ->
                        ho2.getDistance() < ho1.getDistance() ? 1 : -1);
                listRoom.setAdapter(new BookRoomAdapter(listHotelOffer));
                Log.d("load data", "listHotelOffer != null");
            }
        });
    }

    public void init() {
        listRoom = findViewById(R.id.listRoom);
        textNotification = findViewById(R.id.textNotification);
        utilityBarView = findViewById(R.id.utilityBarView);


        bookRoomViewModel = new ViewModelProvider(this).get(BookRoomViewModel.class);
    }
}
