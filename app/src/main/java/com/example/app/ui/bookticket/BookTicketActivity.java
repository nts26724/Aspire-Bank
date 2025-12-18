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

import com.example.app.R;
import com.example.app.ui.customeview.UtilityBarView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;

public class BookTicketActivity extends AppCompatActivity {
    private TextInputEditText origin, destination, departureDay,
            quantityAdult, quantityChildren;
    private TextView find;
    private UtilityBarView utilityBarView;

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
            if(origin.getText() == null || destination.getText() == null ||
                    departureDay.getText() == null || quantityAdult.getText() == null
                    || quantityChildren.getText() == null) {

                Toast.makeText(this,
                        "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }


            String originStr = origin.getText().toString();
            String destinationStr = destination.getText().toString();
            String departureDayStr = departureDay.getText().toString();
            String quantityAdultStr = quantityAdult.getText().toString();
            String quantityChildrenStr = quantityChildren.getText().toString();


            if (!isValidDate(departureDayStr)) {
                Toast.makeText(this,
                        "Vui lòng nhập ngày theo định dạng yyyy-MM-dd",
                        Toast.LENGTH_SHORT).show();
                return;
            }


            if(!isIATACode(originStr) || !isIATACode(destinationStr)) {
                Toast.makeText(this,
                        "Vui lòng nhập mã sân bay theo định dạng IATA",
                        Toast.LENGTH_SHORT);
                return;
            }


            Intent intentBookTicketList = new Intent(this, BookTicketList.class);
            intentBookTicketList.putExtra("origin", originStr);
            intentBookTicketList.putExtra("destination", destinationStr);
            intentBookTicketList.putExtra("departureDate", departureDayStr);
            intentBookTicketList.putExtra("quantityAdult", quantityAdultStr);
            intentBookTicketList.putExtra("quantityChildren", quantityChildrenStr);
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


    public boolean isValidDate(String input) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isIATACode(String input) {
        return input.matches("^[A-Z]{3}$");
    }

}
