package com.example.app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.data.model.Account;
import com.example.app.data.model.Customer;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.CustomerCallback;
import com.example.app.ui.customeview.AppBarView;
import com.example.app.ui.customeview.NavBarView;
import com.example.app.ui.login.LoginActivity;
import com.example.app.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private AppBarView appBar;
    private NavBarView navBar;
    private ImageView imgAvatar;
    private TextView tvProfileName;
    private Button btnUploadPhoto, btnLogout;
    private TextInputEditText etName, etDob, etAddress, etPhone, etEmail;

    private FireStoreSource fireStoreSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_fragment);

        fireStoreSource = new FireStoreSource();

        initViews();
        loadUserData();
        setupListeners();
    }

    private void initViews() {
        appBar = findViewById(R.id.app_bar);
        navBar = findViewById(R.id.nav_bar);
        imgAvatar = findViewById(R.id.img_avatar);
        tvProfileName = findViewById(R.id.tv_profile_name);
        btnUploadPhoto = findViewById(R.id.btn_upload_photo);
        btnLogout = findViewById(R.id.btn_logout);

        etName = findViewById(R.id.et_profile_name);
        etDob = findViewById(R.id.et_profile_dob);
        etAddress = findViewById(R.id.et_profile_address);
        etPhone = findViewById(R.id.et_profile_phone);
        etEmail = findViewById(R.id.et_profile_email);
    }

    private void loadUserData() {
        Account account = SessionManager.getInstance(this).getAccount();

        if (account != null) {
            tvProfileName.setText(account.getUsername());
            fireStoreSource.getCustomerDetail(account.getUsername(), new CustomerCallback() {
                @Override
                public void onSuccess(Customer customer) {
                    if (customer != null) {
                        if (etName != null) etName.setText(customer.getFullName());
                        if (etPhone != null) etPhone.setText(customer.getPhoneNumber());
                        if (etEmail != null) etEmail.setText(customer.getEmail());
                        if (etDob != null) etDob.setText(customer.getBirthDay());
                        if (etAddress != null) etAddress.setText(customer.getAddress());
                    }
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> {
            SessionManager.getInstance(this).logout();

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnUploadPhoto.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }
}