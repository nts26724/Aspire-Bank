package com.example.app.ui.bookroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.example.app.ui.customeview.UtilityBarView;
import com.google.android.material.textfield.TextInputEditText;

public class BookRoomActivity extends AppCompatActivity {
    TextInputEditText checkInDate, checkOutDate, quantityPeople, quantityRoom, radius;
    CardView find;
    UtilityBarView utilityBarView;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_room_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.book_room), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        utilityBarView.setColorBookRoom();

        find.setOnClickListener(v -> {
            String checkInDateStr = checkInDate.getText().toString();
            String checkOutDateStr = checkOutDate.getText().toString();
            String quantityPeopleStr = quantityPeople.getText().toString();
            String quantityRoomStr = quantityRoom.getText().toString();
            String radiusStr = radius.getText().toString();

            if (checkInDateStr.isEmpty() ||
                    checkOutDateStr.isEmpty() ||
                    quantityPeopleStr.isEmpty() ||
                    quantityRoomStr.isEmpty() ||
                    radiusStr.isEmpty()) {

                Toast.makeText(this,
                        "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();

            } else if(Integer.parseInt(quantityPeopleStr) > 9 ||
                    Integer.parseInt(quantityPeopleStr) < 1) {

                Toast.makeText(this,
                        "Số người phải trong khoảng [0-9]", Toast.LENGTH_SHORT).show();

            } else if(Integer.parseInt(quantityRoomStr) > 9 ||
                    Integer.parseInt(quantityRoomStr) < 1) {

                Toast.makeText(this,
                        "Số phòng phải trong khoảng [0-9]", Toast.LENGTH_SHORT).show();

            } else {
                Intent intentBookRoomMap = new Intent(this, BookRoomMap.class);
                intentBookRoomMap.putExtra("checkInDate", checkInDateStr);
                intentBookRoomMap.putExtra("checkOutDate", checkOutDateStr);
                intentBookRoomMap.putExtra("quantityPeople", quantityPeopleStr);
                intentBookRoomMap.putExtra("quantityRoom", quantityRoomStr);
                intentBookRoomMap.putExtra("radius", radiusStr);
                startActivity(intentBookRoomMap);
            }
        });
    }

    public void init() {
        checkInDate = findViewById(R.id.checkInDate);
        checkOutDate = findViewById(R.id.checkOutDate);
        quantityPeople = findViewById(R.id.quantityPeople);
        quantityRoom = findViewById(R.id.quantityRoom);
        radius = findViewById(R.id.radius);
        utilityBarView = findViewById(R.id.utilityBarView);

        find = findViewById(R.id.find);
    }
}
