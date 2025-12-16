package com.example.app.data.repository;

import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.LoginCallback;

public class AccountRepository {
    private FireStoreSource fireStoreSource;

    public AccountRepository() {
        fireStoreSource = new FireStoreSource();
    }

    public void getUserByUsername(String username, LoginCallback loginCallback) {
        fireStoreSource.getAccountByUsername(username, loginCallback);
    }
}
