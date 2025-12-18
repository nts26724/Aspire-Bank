package com.example.app.ui.listcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class ListCustomerDetail extends AppCompatActivity {
    private TextView edit;
    private TextInputEditText name, birthDay,
            phoneNumber, address, email, gender,
            username, password;
    private LinearLayout usernameLayout, passwordLayout;
    private ListCustomerViewModel listCustomerViewModel;
    private boolean isEdit = false;
    private String usernameCustomerStr;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.list_customer_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list_customer_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Intent intentSource = getIntent();
        usernameCustomerStr = intentSource.getStringExtra("username");

        if(usernameCustomerStr == null) {
            addState();
        } else {
            detailState();
        }
    }

    public void init(){
        name = findViewById(R.id.name);
        birthDay = findViewById(R.id.birthDay);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        edit = findViewById(R.id.edit);
        gender = findViewById(R.id.gender);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);

        listCustomerViewModel = new ViewModelProvider(this).get(ListCustomerViewModel.class);
    }


    public void detailState() {
        listCustomerViewModel.getCustomerByUsername(usernameCustomerStr);

        listCustomerViewModel.getCustomerByUsernameLiveData().observe(
                this, customer -> {
                    name.setText(customer.getFullName());
                    gender.setText(customer.getGender());
                    birthDay.setText(customer.getBirthDay());
                    phoneNumber.setText(customer.getPhoneNumber());
                    address.setText(customer.getAddress());
                    email.setText(customer.getEmail());
                }
        );


        edit.setOnClickListener(v -> {
            if(!isEdit) {
                edit.setText("Lưu");
                name.setEnabled(true);
                gender.setEnabled(true);
                birthDay.setEnabled(true);
                phoneNumber.setEnabled(true);
                address.setEnabled(true);
                email.setEnabled(true);
                isEdit = true;
                return;
            }

            edit.setText("Chỉnh sửa");
            name.setEnabled(false);
            gender.setEnabled(false);
            birthDay.setEnabled(false);
            phoneNumber.setEnabled(false);
            address.setEnabled(false);
            email.setEnabled(false);
            isEdit = false;

            listCustomerViewModel.updateCustomer(
                    usernameCustomerStr,
                    name.getText().toString(),
                    gender.getText().toString(),
                    birthDay.getText().toString(),
                    phoneNumber.getText().toString(),
                    address.getText().toString(),
                    email.getText().toString()
            );

            listCustomerViewModel.isUpdateCustomerSuccess().observe(
                    this, isUpdateOfficerLiveData -> {
                        if(isUpdateOfficerLiveData) {
                            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            Log.d("Update officer", "Update officer: True");
                        } else {
                            Toast.makeText(this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                            Log.d("Update officer", "Update officer: False");
                        }
                    });
        });
    }


    public void addState() {
        edit.setText("Thêm");
        username.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        name.setEnabled(true);
        gender.setEnabled(true);
        birthDay.setEnabled(true);
        phoneNumber.setEnabled(true);
        address.setEnabled(true);
        email.setEnabled(true);

        edit.setOnClickListener(v -> {
            listCustomerViewModel.registerUser(
                    username.getText().toString(),
                    password.getText().toString(),
                    name.getText().toString(),
                    gender.getText().toString(),
                    birthDay.getText().toString(),
                    phoneNumber.getText().toString(),
                    address.getText().toString(),
                    email.getText().toString()
            );

            listCustomerViewModel.isRegisterUserSuccess().observe(
                    this, account -> {
                        if(isRegisterUserSuccess) {
                            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            Log.d("addState", "addState: True");
                        } else {
                            Toast.makeText(this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                            Log.d("addState", "addState: False");
                        }
                    }
            );
        });
    }
}
