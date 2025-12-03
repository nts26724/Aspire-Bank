package com.example.app.ui.bookticket;

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
import com.example.app.ui.customeview.UtilityBarView;
import com.google.android.material.textfield.TextInputEditText;

public class BookTicketActivity extends AppCompatActivity {
    private TextInputEditText origin, destination, departureDay,
            quantityAdult, quantityChildren;
    private TextView find;
    UtilityBarView utilityBarView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_ticket_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.book_ticket), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        utilityBarView.setColorBookTicket();

        find.setOnClickListener(v -> {
            // check
            Intent intentBookTicketList = new Intent(this, BookTicketList.class);
            intentBookTicketList.putExtra("origin", origin.getText().toString());
            intentBookTicketList.putExtra("destination", destination.getText().toString());
            intentBookTicketList.putExtra("departureDate", departureDay.getText().toString());
            intentBookTicketList.putExtra("quantityAdult", quantityAdult.getText().toString());
            intentBookTicketList.putExtra("quantityChildren", quantityChildren.getText().toString());
            startActivity(intentBookTicketList);
        });
    }

    public void init() {
        origin = findViewById(R.id.origin);
        destination = findViewById(R.id.desination);
        departureDay = findViewById(R.id.departureDay);
        quantityAdult = findViewById(R.id.quantityAdult);
        quantityChildren = findViewById(R.id.quantityChildren);

        find = findViewById(R.id.find);

        utilityBarView = findViewById(R.id.utilityBarView);
    }
}
