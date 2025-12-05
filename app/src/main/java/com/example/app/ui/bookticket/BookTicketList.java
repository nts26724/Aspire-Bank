package com.example.app.ui.bookticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.adapter.BookTicketAdapter;
import com.example.app.ui.customeview.UtilityBarView;

import java.util.Collections;

public class BookTicketList extends AppCompatActivity {
    private RecyclerView listTicket;
    private BookTicketViewModel bookTicketViewModel;
    private UtilityBarView utilityBarView;
    private TextView textNotification;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_ticket_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.book_ticket_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        utilityBarView.setColorBookTicket();

        Intent intentSource = getIntent();

        String originStr = intentSource.getStringExtra("origin");
        String destinationStr = intentSource.getStringExtra("destination");
        String departureDateStr = intentSource.getStringExtra("departureDate");
        String quantityAdultStr = intentSource.getStringExtra("quantityAdult");
        String quantityChildrenStr = intentSource.getStringExtra("quantityChildren");


        bookTicketViewModel.getFlightOffers(originStr, destinationStr,
                departureDateStr, quantityAdultStr, quantityChildrenStr);

        bookTicketViewModel.getListFlightOfferLiveData().observe(this,
                listFlightOffer -> {
            if(listFlightOffer == null || listFlightOffer.isEmpty()) {
                listTicket.setVisibility(View.GONE);
                textNotification.setVisibility(View.VISIBLE);
            } else {
                listTicket.setVisibility(View.VISIBLE);
                textNotification.setVisibility(View.GONE);
                Collections.sort(listFlightOffer, (fo1, fo2) ->
                        Double.parseDouble(fo2.getTotalAmount()) * 25000 <
                        Double.parseDouble(fo1.getTotalAmount()) * 25000
                        ? 1 : -1);
                listTicket.setAdapter(new BookTicketAdapter(listFlightOffer));
            }
        });
    }


    public void init() {
        listTicket = findViewById(R.id.listTicket);
        utilityBarView = findViewById(R.id.utilityBarView);

        bookTicketViewModel = new ViewModelProvider(this).get(BookTicketViewModel.class);

        textNotification = findViewById(R.id.textNotification);
    }
}
