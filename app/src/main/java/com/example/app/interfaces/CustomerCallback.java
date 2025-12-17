package com.example.app.interfaces;

import com.example.app.data.model.Customer;

public interface CustomerCallback {
    void onSuccess(Customer customer);
    void onFailure(String message);
}