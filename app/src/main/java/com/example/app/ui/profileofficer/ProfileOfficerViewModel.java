package com.example.app.ui.profileofficer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Officer;
import com.example.app.data.repository.ProfileOfficerRepository;

public class ProfileOfficerViewModel extends AndroidViewModel {
    private ProfileOfficerRepository profileOfficerRepository;
    private MutableLiveData<Officer> officerByUserNameLiveData;
    private MutableLiveData<Boolean> isUpdateOfficerSuccess;

    public ProfileOfficerViewModel(@NonNull Application application) {
        super(application);

        profileOfficerRepository = new ProfileOfficerRepository();
        officerByUserNameLiveData = new MutableLiveData<>();
        isUpdateOfficerSuccess = new MutableLiveData<>();
    }

    public void getOfficerByUserName(String username) {
        profileOfficerRepository.getOfficerByUserName(username, officerByUserNameLiveData);
    }

    public MutableLiveData<Officer> getOfficerByUserNameLiveData() {
        return officerByUserNameLiveData;
    }

    public void updateOfficer(String username, String fullName,
                              String birthDay, String phoneNumber,
                              String address, String email, String gender) {
        profileOfficerRepository.updateOfficer(
                username, fullName, birthDay,
                phoneNumber, address, email,
                gender,
                isUpdateOfficerSuccess
        );

    }


    public MutableLiveData<Boolean> isUpdateOfficerSuccess() {
        return isUpdateOfficerSuccess;
    }
}