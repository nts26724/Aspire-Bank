package com.example.app.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.data.model.Account;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.LoginCallback;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etUsername, etEmail, etPhone, etBirthday, etAddress, etPassword, etRePassword;
    private RadioGroup rgGender;
    private Button btnRegister;
    private TextView tvLoginNow;

    private RegisterViewModel registerViewModel;
    private FireStoreSource fireStoreSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        fireStoreSource = new FireStoreSource();

        initViews();
        setupDateInputFormat(etBirthday);
        setupListeners();
        setupObservers();
    }

    private void initViews() {
        etFullName = findViewById(R.id.et_fullname);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etBirthday = findViewById(R.id.et_birthday);
        etAddress = findViewById(R.id.et_address);
        etPassword = findViewById(R.id.et_password);
        etRePassword = findViewById(R.id.et_re_password);

        rgGender = findViewById(R.id.rg_gender);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginNow = findViewById(R.id.tv_login_now);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String rePassword = etRePassword.getText().toString().trim();

            String gender = "M";
            if (rgGender.getCheckedRadioButtonId() == R.id.rb_female) {
                gender = "F";
            }

            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() ||
                    phone.isEmpty() || birthday.isEmpty() || address.isEmpty() ||
                    password.isEmpty() || rePassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.contains("@")) {
                Toast.makeText(this, "Email không hợp lệ (thiếu @)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!phone.matches("^0\\d{9}$")) {
                Toast.makeText(this, "Số điện thoại phải có 10 số và bắt đầu bằng số 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkAge(birthday)) {
                return;
            }

            if (!password.equals(rePassword)) {
                Toast.makeText(this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            checkUsernameAndRegister(username, password, rePassword, fullName, phone, email, address, birthday, gender);
        });

        tvLoginNow.setOnClickListener(v -> finish());
    }

    private void checkUsernameAndRegister(String username, String password, String rePassword,
                                          String fullName, String phone, String email,
                                          String address, String birthday, String gender) {

        fireStoreSource.getAccountByUsername(username, new LoginCallback() {
            @Override
            public void onSuccess(Account account) {
                if (account != null) {
                    Toast.makeText(RegisterActivity.this, "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác!", Toast.LENGTH_SHORT).show();
                } else {
                    registerViewModel.register(
                            username,
                            password,
                            rePassword,
                            fullName,
                            phone,
                            email,
                            address,
                            birthday,
                            gender
                    );
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(RegisterActivity.this, "Lỗi kiểm tra tài khoản, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkAge(String birthday) {
        try {
            String[] parts = birthday.split("/");
            if (parts.length != 3) {
                Toast.makeText(this, "Ngày sinh không đúng định dạng", Toast.LENGTH_SHORT).show();
                return false;
            }

            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            Calendar dob = Calendar.getInstance();
            dob.set(year, month - 1, day);

            Calendar today = Calendar.getInstance();

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            if (age < 16) {
                Toast.makeText(this, "Bạn chưa đủ 16 tuổi để đăng ký!", Toast.LENGTH_LONG).show();
                return false;
            }

            if (year > today.get(Calendar.YEAR)) {
                Toast.makeText(this, "Năm sinh không hợp lệ!", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi kiểm tra ngày sinh", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setupObservers() {
        registerViewModel.getRegisterResult().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, RegisterFaceAuthActivity.class);
                intent.putExtra("FULL_NAME", etFullName.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });

        registerViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDateInputFormat(final EditText etDate) {
        etDate.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year < 1900) ? 1900 : (year > 2009) ? 2009 : year;
                        cal.set(Calendar.YEAR, year);

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        day = (day < 1) ? 1 : day;

                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    etDate.setText(current);
                    etDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}