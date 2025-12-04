package com.example.app.interfaces;

import com.example.app.data.model.Transaction;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface TransactionCallback {
    void onSuccess(List<Transaction> transactions);
    void onFailure();

}
