package com.example.app.ui.profileofficer;

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
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.data.model.Account;
import com.example.app.ui.login.LoginActivity;
import com.example.app.utils.SessionManager;

public class ProfileOfficerActivity extends AppCompatActivity {
    private TextView nameAvatar, edit, logout, gender,
            inputName, birthDay, phoneNumber, address, email;
    private ProfileOfficerViewModel profileOfficerViewModel;
    private boolean isEdit = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile_officer_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_officer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();


        profileOfficerViewModel.getOfficerByUserName(
                SessionManager.getInstance(this).getAccount().getUsername()
        );

        profileOfficerViewModel.getOfficerByUserNameLiveData().observe(
                this, officer -> {
                    nameAvatar.setText(officer.getFullName());
                    inputName.setText(officer.getFullName());
                    gender.setText(officer.getGender());
                    birthDay.setText(officer.getBirthDay());
                    phoneNumber.setText(officer.getPhoneNumber());
                    address.setText(officer.getAddress());
                    email.setText(officer.getEmail());
                }
        );


        edit.setOnClickListener(v -> {
            if(!isEdit) {
                edit.setText("Lưu");
                isEdit = true;

                inputName.setEnabled(true);
                gender.setEnabled(true);
                birthDay.setEnabled(true);
                phoneNumber.setEnabled(true);
                address.setEnabled(true);
                email.setEnabled(true);
                return;
            }

            edit.setText("Chỉnh sửa");
            isEdit = false;

            inputName.setEnabled(false);
            gender.setEnabled(false);
            birthDay.setEnabled(false);
            phoneNumber.setEnabled(false);
            address.setEnabled(false);
            email.setEnabled(false);

            profileOfficerViewModel.updateOfficer(
                SessionManager.getInstance(this).getAccount().getUsername(),
                inputName.getText().toString(),
                birthDay.getText().toString(),
                phoneNumber.getText().toString(),
                address.getText().toString(),
                email.getText().toString(),
                gender.getText().toString()
            );

            profileOfficerViewModel.isUpdateOfficerSuccess().observe(
                    this, isUpdateSuccess -> {
                        if (isUpdateSuccess) {
                            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            Log.d("isUpdateOfficerSuccess", "Cập nhật thành công");
                        } else {
                            Toast.makeText(this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                            Log.d("isUpdateOfficerSuccess", "Cập nhật không thành công");
                        }
                    }
            );
        });

        logout.setOnClickListener(v -> {
            Intent intentLogin = new Intent(this, LoginActivity.class);
            startActivity(intentLogin);
            SessionManager.getInstance(this).logout();
        });
    }

    public void init() {
        nameAvatar = findViewById(R.id.nameAvatar);
        inputName = findViewById(R.id.inputName);
        birthDay = findViewById(R.id.birthDay);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);

        edit = findViewById(R.id.edit);
        logout = findViewById(R.id.logout);

        profileOfficerViewModel = new ViewModelProvider(this).get(ProfileOfficerViewModel.class);
    }
}
