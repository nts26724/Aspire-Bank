package com.example.app.ui.homeofficer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Officer;
import com.example.app.data.repository.HomeOfficerRepository;
import com.example.app.interfaces.HomeCustomerCallback;

public class HomeOfficerViewModel extends AndroidViewModel {
    private HomeOfficerRepository homeOfficerRepository;
    private MutableLiveData<String> numberOfCustomerLiveData;
    private MutableLiveData<String> numberOfOfficerLiveData;
    private MutableLiveData<String> rateLiveData;
    private MutableLiveData<Officer> officerByUserNameLiveData;

    public HomeOfficerViewModel(@NonNull Application application) {
        super(application);

        homeOfficerRepository = new HomeOfficerRepository();
        numberOfCustomerLiveData = new MutableLiveData<>();
        numberOfOfficerLiveData = new MutableLiveData<>();
        rateLiveData = new MutableLiveData<>();
        officerByUserNameLiveData = new MutableLiveData<>();
    }

    public void getNumberOfCustomer() {
        homeOfficerRepository.getNumberOfCustomer(numberOfCustomerLiveData);
    }

    public MutableLiveData<String> getNumberOfCustomerLiveData() {
        return numberOfCustomerLiveData;
    }

    public void getNumberOfOfficer() {
        homeOfficerRepository.getNumberOfOfficer(numberOfOfficerLiveData);
    }

    public MutableLiveData<String> getNumberOfOfficerLiveData() {
        return numberOfOfficerLiveData;
    }

    public void getRate() {
        homeOfficerRepository.getRate(rateLiveData);
    }

    public MutableLiveData<String> getRateLiveData() {
        return rateLiveData;
    }

    public MutableLiveData<Officer> getFullNameLiveData() {
        return officerByUserNameLiveData;
    }

    public void getOfficerByUserName(String username) {
        homeOfficerRepository.getOfficerByUserName(username, officerByUserNameLiveData);
    }
}
