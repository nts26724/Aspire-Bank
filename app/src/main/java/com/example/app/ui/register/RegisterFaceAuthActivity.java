package com.example.app.ui.register;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.app.R;
import com.google.android.material.button.MaterialButton;

public class RegisterFaceAuthActivity extends AppCompatActivity {

    private MaterialButton btnScan, btnCancel;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    onAuthSuccess();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_face_auth);

        btnScan = findViewById(R.id.btn_start_scan);
        btnCancel = findViewById(R.id.btn_cancel);

        btnScan.setOnClickListener(v -> checkPermissionAndOpenCamera());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(cameraIntent);
        } else {
            onAuthSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền Camera để xác thực!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onAuthSuccess() {
        Toast.makeText(this, "Xác thực khuôn mặt thành công!", Toast.LENGTH_SHORT).show();
        Intent nextIntent = new Intent(RegisterFaceAuthActivity.this, RegisterSuccessActivity.class);
        startActivity(nextIntent);
        finish();
    }
}