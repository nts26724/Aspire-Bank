package com.example.app.ui.rate;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class RateActivity extends AppCompatActivity {
    private TextView textRate, save;
    private TextInputEditText inputRate;
    private RateViewModel rateViewModel;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.rate_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rate), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        rateViewModel.getRate();

        rateViewModel.getRateLiveData().observe(this, rateLiveData -> {
            textRate.setText(rateLiveData);
            inputRate.setText(rateLiveData.split(" ")[0]);
        });

        save.setOnClickListener(v -> {
            rateViewModel.updateRate(inputRate.getText().toString());

            rateViewModel.isUpdateRateLiveData().observe(this, isUpdateRateLiveData -> {
                if(isUpdateRateLiveData) {
                    textRate.setText(inputRate.getText() + " %");
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void init() {
        textRate = findViewById(R.id.textRate);
        save = findViewById(R.id.save);
        inputRate = findViewById(R.id.inputRate);

        rateViewModel = new ViewModelProvider(this).get(RateViewModel.class);
    }
}
