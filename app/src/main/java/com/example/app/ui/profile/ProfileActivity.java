package com.example.app.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private AppBarView appBar;
    private NavBarView navBar;
    private ImageView imgAvatar;
    private TextView tvProfileName;
    private Button btnUploadPhoto, btnLogout;
    private TextInputEditText etName, etDob, etAddress, etPhone, etEmail;

    private FireStoreSource fireStoreSource;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_fragment);

        fireStoreSource = new FireStoreSource();

        initViews();
        setupImagePicker();
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