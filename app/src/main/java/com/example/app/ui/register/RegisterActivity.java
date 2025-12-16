package com.example.app.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.ui.homecustomer.HomeCustomerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etUsername, etEmail, etPhone, etBirthday, etAddress, etPassword, etRePassword;
    private RadioGroup rgGender;
    private Button btnRegister;
    private TextView tvLoginNow;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        setupListeners();
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
            if (validateInputs()) {
                generateUniqueAccountNumberAndRegister();
            }
        });

        tvLoginNow.setOnClickListener(v -> finish());
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etEmail.getText()) || TextUtils.isEmpty(etPassword.getText())) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etFullName.getText()) || TextUtils.isEmpty(etUsername.getText())) {
            Toast.makeText(this, "Vui lòng nhập họ tên và tên đăng nhập", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!etPassword.getText().toString().equals(etRePassword.getText().toString())) {
            Toast.makeText(this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void generateUniqueAccountNumberAndRegister() {
        String randomNumber = generateRandomNumberString(12);

        db.collection("account")
                .whereEqualTo("accountNumber", randomNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot != null && !snapshot.isEmpty()) {
                            generateUniqueAccountNumberAndRegister();
                        } else {
                            registerUser(randomNumber);
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String generateRandomNumberString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private void registerUser(String uniqueAccountNumber) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveDataToFirestore(user.getUid(), uniqueAccountNumber);
                        }
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định";
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveDataToFirestore(String userId, String accountNumber) {
        String fullName = etFullName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String birthday = etBirthday.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        String gender = "M";
        if (rgGender.getCheckedRadioButtonId() == R.id.rb_female) {
            gender = "F";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 5);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String expiryDate = sdf.format(calendar.getTime());

        Map<String, Object> accountMap = new HashMap<>();
        accountMap.put("username", username);
        accountMap.put("password", password);
        accountMap.put("role", "customer");
        accountMap.put("balance", 0);
        accountMap.put("mortgage", 0);
        accountMap.put("saving", 0);
        accountMap.put("accountNumber", accountNumber);
        accountMap.put("cardNumber", generateRandomNumberString(16));
        accountMap.put("expiryDate", expiryDate);

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("fullName", fullName);
        customerMap.put("username", username);
        customerMap.put("email", email);
        customerMap.put("phoneNumber", phone);
        customerMap.put("address", address);
        customerMap.put("birthDay", birthday);
        customerMap.put("gender", gender);
        customerMap.put("cccd", "000000000000");

        db.collection("account")
                .add(accountMap)
                .addOnSuccessListener(documentReference -> {
                    String accountDocId = documentReference.getId();
                    customerMap.put("account", accountDocId);

                    db.collection("customer")
                            .document(userId)
                            .set(customerMap)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, RegisterFaceAuthActivity.class);
                                intent.putExtra("FULL_NAME", fullName);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Lỗi lưu Customer", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi lưu Account", Toast.LENGTH_SHORT).show());
    }
}