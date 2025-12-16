package com.example.app.data.repository;

import com.example.app.data.model.User;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.UserCallback;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private FireStoreSource fireStoreSource;
    private static List<User> localUsers = new ArrayList<>();

    static {
        localUsers.add(new User("0909000000", "Admin User", "00123456789", "admin@aspire.com"));
    }

    public UserRepository() {
        fireStoreSource = new FireStoreSource();
    }

    public void registerUser(User user, UserCallback callback) {
        fireStoreSource.registerUser(user, callback);
    }

    public void login(String username, String password, UserCallback callback) {
        for (User u : localUsers) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                callback.onUserLoaded(u);
                return;
            }
        }
        callback.onError("Sai tài khoản hoặc mật khẩu");
    }
}