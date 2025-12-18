package com.example.app.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.app.data.remote.FireStoreSource;

public class RateRepository {
    private FireStoreSource fireStoreSource;

    public RateRepository() {
        fireStoreSource = new FireStoreSource();
    }

    public void getRate(MutableLiveData<String> rateLiveData) {
        fireStoreSource.getRate(rateLiveData);
    }

    public void updateRate(String rate, MutableLiveData<Boolean> isUpdateRateLiveData) {
        fireStoreSource.updateRate(rate, isUpdateRateLiveData);
    }
}
