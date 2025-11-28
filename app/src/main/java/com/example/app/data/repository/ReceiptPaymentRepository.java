package com.example.app.data.repository;

import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.ReceiptPaymentCallback;

public class ReceiptPaymentRepository {
    private FireStoreSource fireStoreSource;

    public ReceiptPaymentRepository() {
        fireStoreSource = new FireStoreSource();
    }

    public void getReceiptsByUsername(String username, ReceiptPaymentCallback receiptPaymentCallback) {
        fireStoreSource.getReceiptByUsername(username, receiptPaymentCallback);
    }
}
