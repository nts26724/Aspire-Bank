package com.example.app.ui.rate;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.repository.RateRepository;

public class RateViewModel extends AndroidViewModel {
    private RateRepository rateRepository;
    private MutableLiveData<String> rateLiveData;
    private MutableLiveData<Boolean> isUpdateRateLiveData;


    public RateViewModel(@NonNull Application application) {
        super(application);

        rateRepository = new RateRepository();
        rateLiveData = new MutableLiveData<>();
        isUpdateRateLiveData = new MutableLiveData<>();
    }

    public void getRate() {
        rateRepository.getRate(rateLiveData);
    }

    public MutableLiveData<String> getRateLiveData() {
        return rateLiveData;
    }

    public void updateRate(String rate) {
        rateRepository.updateRate(rate, isUpdateRateLiveData);
    }

    public MutableLiveData<Boolean> isUpdateRateLiveData() {
        return isUpdateRateLiveData;
    }
}
