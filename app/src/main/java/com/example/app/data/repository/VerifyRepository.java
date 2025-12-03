package com.example.app.data.repository;

import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.PhoneNumberCallBack;

public class VerifyRepository {
    private FireStoreSource fireStoreSource;

    public VerifyRepository() {
        fireStoreSource = new FireStoreSource();
    }


    public void getPhoneNumberByUsername(String username, PhoneNumberCallBack phoneNumberCallBack) {
        fireStoreSource.getPhoneNumberByUsername(username, phoneNumberCallBack);
    }
}
