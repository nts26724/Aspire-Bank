package com.example.app.ui.bookroom;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.google.android.material.textfield.TextInputEditText;

public class BookRoomActivity extends AppCompatActivity {
    TextInputEditText source, desination, departureDay, returnDay;
    TextView find;

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

        find.setOnClickListener(v -> {
            String sourceStr = source.getText().toString();
            String desinationStr = desination.getText().toString();
            String departureDayStr = departureDay.getText().toString();
            String returnDayStr = returnDay.getText().toString();


        });
    }

    public void init() {
        source = findViewById(R.id.source);
        desination = findViewById(R.id.desination);
        departureDay = findViewById(R.id.departureDay);
        returnDay = findViewById(R.id.returnDay);

        find = findViewById(R.id.find);
    }
}
