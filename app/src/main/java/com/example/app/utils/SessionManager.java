package com.example.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.app.data.model.Account;
import com.google.gson.Gson;

public class SessionManager {
    private static SessionManager instance;
    private Account account;
    private static final String PREF_NAME = "app_session";
    private static final String KEY_ACCOUNT = "account_json";
    private final SharedPreferences prefs;


    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);

        String json = prefs.getString(KEY_ACCOUNT, null);
        if (json != null) {
            account = new Gson().fromJson(json, Account.class);
        }
    }

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setAccount(Account account) {
        this.account = account;

        String json = new Gson().toJson(account);
        prefs.edit().putString(KEY_ACCOUNT, json).apply();
    }

    public Account getAccount() {
        return account;
    }

    public void clear() {
        account = null;
        prefs.edit().clear().apply();
    }
}
