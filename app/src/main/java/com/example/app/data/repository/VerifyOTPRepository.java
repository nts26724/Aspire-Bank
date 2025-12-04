package com.example.app.data.repository;

import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.LoginCallback;
import com.example.app.interfaces.PhoneNumberCallBack;

public class VerifyOTPRepository {
    private FireStoreSource fireStoreSource;

    public VerifyOTPRepository() {
        fireStoreSource = new FireStoreSource();
    }


    public void getPhoneNumberByUsername(String username, PhoneNumberCallBack phoneNumberCallBack) {
        fireStoreSource.getPhoneNumberByUsername(username, phoneNumberCallBack);
    }


    public void widthraw(String username, long amount) {
        fireStoreSource.widthraw(username, amount);
    }

    public void addTransaction(long amount, String content, boolean transfer,
                                String usernameTransfer, String usernameReceive) {
        fireStoreSource.addTransaction(amount, content, transfer, usernameTransfer, usernameReceive);
    }


    public void deleteReceiptByReceiptID(String receiptID) {
        fireStoreSource.deleteReceiptByReceiptID(receiptID);
    }

    public void getAccountByUsername(String username, LoginCallback loginCallback) {
        fireStoreSource.getAccountByUsername(username, loginCallback);
    }
}
