package com.example.app.interfaces;

import com.example.app.data.model.User;

public interface UserCallback {
    void onSuccess();
    void onUserLoaded(User user);
    void onError(String message);
}