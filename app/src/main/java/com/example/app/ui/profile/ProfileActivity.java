package com.example.app.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns; // Import thêm thư viện này
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private AppBarView appBar;
    private NavBarView navBar;
    private ImageView imgAvatar;
    private TextView tvProfileName;
    private Button btnUploadPhoto, btnLogout, btnEdit;
    private TextInputEditText etName, etDob, etAddress, etPhone, etEmail;

    private FireStoreSource fireStoreSource;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_fragment);

        fireStoreSource = new FireStoreSource();

        initViews();
        setupImagePicker();
        setupDateInputFormat(etDob);
        loadUserData();
        setupListeners();

        setEditMode(false);
    }

    private void initViews() {
        appBar = findViewById(R.id.app_bar);
        navBar = findViewById(R.id.nav_bar);
        imgAvatar = findViewById(R.id.img_avatar);
        tvProfileName = findViewById(R.id.tv_profile_name);
        btnUploadPhoto = findViewById(R.id.btn_upload_photo);
        btnLogout = findViewById(R.id.btn_logout);
        btnEdit = findViewById(R.id.btn_edit_profile);

        etName = findViewById(R.id.et_profile_name);
        etDob = findViewById(R.id.et_profile_dob);
        etAddress = findViewById(R.id.et_profile_address);
        etPhone = findViewById(R.id.et_profile_phone);
        etEmail = findViewById(R.id.et_profile_email);
    }

    private void setupImagePicker() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                processAndSaveImage(uri);
            }
        });
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

                        String avatarData = customer.getAvatarUrl();
                        if (avatarData != null && !avatarData.isEmpty()) {
                            if (avatarData.startsWith("data:image")) {
                                try {
                                    String pureBase64 = avatarData.substring(avatarData.indexOf(",") + 1);
                                    byte[] decodedString = Base64.decode(pureBase64, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    imgAvatar.setImageBitmap(decodedByte);
                                } catch (Exception e) {
                                    Glide.with(ProfileActivity.this).load(avatarData).into(imgAvatar);
                                }
                            } else {
                                Glide.with(ProfileActivity.this).load(avatarData).into(imgAvatar);
                            }
                        }
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
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                if (isEditing) {
                    validateAndSaveProfile();
                } else {
                    setEditMode(true);
                }
            });
        }
    }

    private void setEditMode(boolean enable) {
        isEditing = enable;

        etName.setEnabled(enable);
        etDob.setEnabled(enable);
        etAddress.setEnabled(enable);
        etPhone.setEnabled(enable);
        etEmail.setEnabled(enable);

        if (enable) {
            btnEdit.setText("Lưu thông tin");
            etName.requestFocus();
        } else {
            btnEdit.setText("Chỉnh sửa");
        }
    }

    private void validateAndSaveProfile() {
        String fullName = etName.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (fullName.isEmpty() || dob.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng không để trống thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches("^0\\d{9}$")) {
            Toast.makeText(this, "Số điện thoại phải có 10 số và bắt đầu bằng số 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkAge(dob)) {
            return;
        }

        updateUserProfile(fullName, dob, address, phone, email);
    }

    private void updateUserProfile(String fullName, String dob, String address, String phone, String email) {
        Account account = SessionManager.getInstance(this).getAccount();
        if (account == null) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("customer")
                .whereEqualTo("username", account.getUsername())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("fullName", fullName);
                        updates.put("birthDay", dob);
                        updates.put("address", address);
                        updates.put("phoneNumber", phone);
                        updates.put("email", email);

                        db.collection("customer").document(document.getId())
                                .update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(ProfileActivity.this, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                                    setEditMode(false);
                                })
                                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Lỗi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Không tìm thấy dữ liệu người dùng", Toast.LENGTH_SHORT).show());
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
                Toast.makeText(this, "Bạn chưa đủ 16 tuổi!", Toast.LENGTH_LONG).show();
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

    private void processAndSaveImage(Uri imageUri) {
        Toast.makeText(this, "Đang xử lý ảnh...", Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            try {
                Bitmap tempBitmap;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    tempBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
                } else {
                    tempBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                }

                Bitmap finalBitmap = getResizedBitmap(tempBitmap, 500);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                String finalBase64String = "data:image/jpeg;base64," + encodedImage;

                runOnUiThread(() -> {
                    updateFirestoreAvatar(finalBase64String, finalBitmap);
                });

            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi xử lý ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void updateFirestoreAvatar(String base64String, Bitmap displayBitmap) {
        Account account = SessionManager.getInstance(this).getAccount();
        if (account == null) return;

        fireStoreSource.updateCustomerAvatar(account.getUsername(), base64String, new CustomerCallback() {
            @Override
            public void onSuccess(Customer customer) {
                Toast.makeText(ProfileActivity.this, "Cập nhật ảnh thành công!", Toast.LENGTH_SHORT).show();
                imgAvatar.setImageBitmap(displayBitmap);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(ProfileActivity.this, "Lỗi lưu DB: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}