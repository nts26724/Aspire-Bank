package com.example.app.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Officer;
import com.example.app.data.remote.FireStoreSource;

public class ProfileOfficerRepository {
    private FireStoreSource fireStoreSource;

    public ProfileOfficerRepository() {
        fireStoreSource = new FireStoreSource();
    }

    public void getOfficerByUserName(String username, MutableLiveData<Officer> officerByUserNameLiveData) {
        fireStoreSource.getOfficerByUserName(username, officerByUserNameLiveData);
    }

    public void updateOfficer(String username, String fullName, String birthDay,
                              String phoneNumber, String address, String email,
                              String gender,
                              MutableLiveData<Boolean> isUpdateOfficerSuccess) {
        fireStoreSource.updateOfficer(username, fullName, birthDay,
                phoneNumber, address, email, gender, isUpdateOfficerSuccess);
    }
}
