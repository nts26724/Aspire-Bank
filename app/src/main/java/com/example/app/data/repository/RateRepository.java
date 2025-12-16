package com.example.app.data.repository;

import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.RateCallback;

public class RateRepository {
    private FireStoreSource fireStoreSource;

    public RateRepository() {
        fireStoreSource = new FireStoreSource();
    }

    public void getRateByTerm(int termMonths, RateCallback callback) {
        fireStoreSource.getInterestRates(termMonths, callback);
    }
}