package com.example.app.interfaces;

import com.example.app.data.model.Receipt;

import java.util.List;

public interface ReceiptPaymentCallback {
    void onSuccess(List<Receipt> receipts);
    void onFailure();

}
