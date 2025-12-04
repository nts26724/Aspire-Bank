package com.example.app.interfaces;

public interface TokenCallback {
    void onSuccess(String accessToken);
    void onFailure();

}
