package com.example.app.interfaces;

import com.example.app.data.model.Account;

public interface LoginCallback {
    void onSuccess(Account account);
    void onFailure();
}
