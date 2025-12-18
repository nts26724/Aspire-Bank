package com.example.app.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Officer;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.HomeCustomerCallback;

public class HomeOfficerRepository {
    private FireStoreSource fireStoreSource;
    public HomeOfficerRepository() {
        fireStoreSource = new FireStoreSource();
    }

    public void getNumberOfCustomer(MutableLiveData<String> numberOfCustomerLiveData) {
        fireStoreSource.getNumberOfCustomer(numberOfCustomerLiveData);
    }

    public void getNumberOfOfficer(MutableLiveData<String> numberOfOfficerLiveData) {
        fireStoreSource.getNumberOfOfficer(numberOfOfficerLiveData);
    }

    public void getRate(MutableLiveData<String> rateLiveData) {
        fireStoreSource.getRate(rateLiveData);
    }

    public void getOfficerByUserName(String username, MutableLiveData<Officer> officerByUserNameLiveData) {
        fireStoreSource.getOfficerByUserName(username, officerByUserNameLiveData);
    }
}
