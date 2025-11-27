package com.example.app.data.repository;

import com.example.app.data.model.Transaction;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.TransactionCallback;

public class TransactionRepository {
    private FireStoreSource fireStoreSource;

    public TransactionRepository() {
        fireStoreSource = new FireStoreSource();
    }

    public void getTransactionsByUsername(String username, TransactionCallback transactionCallback) {
        fireStoreSource.getTransactionByUsername(username, transactionCallback);
    }
}
