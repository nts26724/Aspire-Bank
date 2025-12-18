package com.example.app.ui.homeofficer;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.data.model.Account;
import com.example.app.utils.SessionManager;

public class HomeOfficerActivity extends AppCompatActivity {
    private TextView name, email,
            numberOfCustomer, numberOfOfficer, rate;
    private HomeOfficerViewModel homeOfficerViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_officer_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_officer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        homeOfficerViewModel.getOfficerByUserName(
                SessionManager.getInstance(this).getAccount().getUsername()
        );

        homeOfficerViewModel.getFullNameLiveData().observe(this, officer -> {
            name.setText(officer.getFullName());
            email.setText(officer.getEmail());
        });


        homeOfficerViewModel.getNumberOfCustomer();
        homeOfficerViewModel.getNumberOfCustomerLiveData().observe(
                this, numberOfCustomer::setText);

        homeOfficerViewModel.getNumberOfOfficer();
        homeOfficerViewModel.getNumberOfOfficerLiveData().observe(
                this, numberOfOfficer::setText);

        homeOfficerViewModel.getRate();
        homeOfficerViewModel.getRateLiveData().observe(
                this, rate::setText);
    }

    public void init() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        numberOfCustomer = findViewById(R.id.numberOfCustomer);
        numberOfOfficer = findViewById(R.id.numberOfOfficer);
        rate = findViewById(R.id.rate);

        homeOfficerViewModel = new ViewModelProvider(this).get(HomeOfficerViewModel.class);
    }
}
