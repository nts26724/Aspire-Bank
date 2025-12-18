package com.example.app.interfaces;

import com.example.app.data.model.InterestRate;

public interface RateCallback {
    void onRateLoaded(InterestRate rate);
    void onError(String message);
}