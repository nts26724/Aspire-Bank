package com.example.app.interfaces;

public interface ReceiverCallback {
    void onSuccess(String fullName);
    void onFailure(String message);
}