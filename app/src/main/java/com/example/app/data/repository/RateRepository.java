package com.example.app.data.repository;

import com.example.app.data.model.InterestRate;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.RateCallback;

import java.util.ArrayList;
import java.util.List;

public class RateRepository {
    private FireStoreSource fireStoreSource;

    // Giả lập Database cục bộ
    private static List<InterestRate> localRates = new ArrayList<>();
    static {
        localRates.add(new InterestRate(1, 3.5, 8.5));
        localRates.add(new InterestRate(3, 3.8, 8.8));
        localRates.add(new InterestRate(6, 5.5, 9.5));
        localRates.add(new InterestRate(12, 6.8, 10.5));
        localRates.add(new InterestRate(24, 7.2, 11.0));
    }

    public RateRepository() {
        fireStoreSource = new FireStoreSource();
    }

    public void getRateByTerm(int termMonths, RateCallback callback) {
        // TODO: Sau này sẽ gọi fireStoreSource.getRateByTerm(...)

        InterestRate result = null;
        for (InterestRate rate : localRates) {
            if (rate.getTermMonths() == termMonths) {
                result = rate;
                break;
            }
        }

        if (result != null) {
            callback.onRateLoaded(result);
        } else {
            callback.onRateLoaded(new InterestRate(1, 3.5, 8.5));
        }
    }
}