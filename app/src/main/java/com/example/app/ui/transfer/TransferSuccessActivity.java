package com.example.app.ui.transfer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.example.app.R;
import com.example.app.ui.homecustomer.HomeCustomerActivity;
import com.example.app.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.io.OutputStream;
import java.text.DecimalFormat;

public class TransferSuccessActivity extends AppCompatActivity {

    private TextView tvSenderName;
    private TextView tvReceiverName;
    private TextView tvBankName;
    private TextView tvAmount;
    private TextView tvContent;
    private Group groupBankDetails;

    private MaterialButton btnContinue;
    private MaterialButton btnSaveImage;
    private MaterialButton btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_success_activity);

        initViews();
        displayData();
        setupListeners();
    }

    private void initViews() {
        tvSenderName = findViewById(R.id.tv_sender_name);
        tvReceiverName = findViewById(R.id.tv_receiver_name);
        tvBankName = findViewById(R.id.tv_bank_name);
        tvAmount = findViewById(R.id.tv_amount);
        tvContent = findViewById(R.id.tv_content);
        groupBankDetails = findViewById(R.id.group_bank_details);

        btnContinue = findViewById(R.id.btn_continue_transaction);
        btnSaveImage = findViewById(R.id.btn_save_image);
        btnHome = findViewById(R.id.btn_go_home);
    }

    private void displayData() {
        Intent intent = getIntent();
        String transactionType = intent.getStringExtra("TRANSACTION_TYPE");
        double amount = intent.getDoubleExtra("AMOUNT", 0);
        String receiverName = intent.getStringExtra("RECEIVER_NAME");
        String bankName = intent.getStringExtra("BANK_NAME");
        String message = intent.getStringExtra("MESSAGE");

        String senderName = intent.getStringExtra("SENDER_NAME");
        if (senderName != null) {
            tvSenderName.setText(senderName);
        } else {
            if (SessionManager.getInstance(this).getAccount() != null) {
                tvSenderName.setText(SessionManager.getInstance(this).getAccount().getUsername());
            }
        }

        if (receiverName != null) {
            tvReceiverName.setText(receiverName);
        }

        DecimalFormat df = new DecimalFormat("#,###");
        tvAmount.setText(df.format(amount) + " VND");

        if (message != null && !message.isEmpty()) {
            tvContent.setText(message);
        } else {
            tvContent.setText("Chuyển tiền");
        }

        if ("TRANSFER_INTERNAL".equals(transactionType)) {
            groupBankDetails.setVisibility(View.GONE);
        } else {
            groupBankDetails.setVisibility(View.VISIBLE);
            if (bankName != null) {
                tvBankName.setText(bankName);
            }
        }
    }

    private void setupListeners() {
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(TransferSuccessActivity.this, HomeCustomerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(TransferSuccessActivity.this, TransferActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnSaveImage.setOnClickListener(v -> {
            setButtonsVisibility(View.INVISIBLE);
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            rootView.post(() -> {
                captureAndSaveImage(rootView);
                setButtonsVisibility(View.VISIBLE);
            });
        });
    }

    private void setButtonsVisibility(int visibility) {
        btnSaveImage.setVisibility(visibility);
        btnHome.setVisibility(visibility);
        btnContinue.setVisibility(visibility);
    }

    private void captureAndSaveImage(View viewToCapture) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(viewToCapture.getWidth(), viewToCapture.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            Drawable bgDrawable = viewToCapture.getBackground();
            if (bgDrawable != null) {
                bgDrawable.draw(canvas);
            } else {
                canvas.drawColor(Color.WHITE);
            }
            viewToCapture.draw(canvas);

            OutputStream fos;
            String imageName = "Bill_" + System.currentTimeMillis() + ".png";
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();

            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, imageName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/AppBanking");
            }

            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            if (imageUri != null) {
                fos = resolver.openOutputStream(imageUri);
                if (fos != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    Toast.makeText(this, "Đã lưu hóa đơn vào thư viện ảnh!", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}