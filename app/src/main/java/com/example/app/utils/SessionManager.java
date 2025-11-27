package com.example.app.utils;

import com.example.app.data.model.Account;

public class SessionManager {
    private static SessionManager instance;
    private Account account;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setAccount(Account user) {
        this.account = user;
    }

    public Account getAccount() {
        return account;
    }

    public void clear() {
        account = null;
    }
}
