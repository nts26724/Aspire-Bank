package com.example.app.utils;

import com.example.app.data.model.Account;

public class SessionManager {
    private static SessionManager instance;
    private Account currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setUser(Account user) {
        this.currentUser = user;
    }

    public Account getUser() {
        return currentUser;
    }

    public void clear() {
        currentUser = null;
    }
}
