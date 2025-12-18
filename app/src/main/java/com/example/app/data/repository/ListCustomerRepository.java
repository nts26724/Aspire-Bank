package com.example.app.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.model.Customer;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.HomeCustomerCallback;

import java.util.List;

public class ListCustomerRepository {
    private FireStoreSource fireStoreSource;

    public ListCustomerRepository() {
        fireStoreSource = new FireStoreSource();
    }

    public void getListCustomer(MutableLiveData<List<Account>> listCustomerLiveData) {
        fireStoreSource.getListCustomer(listCustomerLiveData);
    }

    public void updateCustomer(String username, String fullName,
                              String birthDay, String phoneNumber,
                              String address, String email, String gender,
                              MutableLiveData<Boolean> isUpdateCustomerSuccess) {
        fireStoreSource.updateCustomer(username, fullName, birthDay,
                phoneNumber, address, email, gender, isUpdateCustomerSuccess);
    }

    public void getCustomerByUsername(String username, MutableLiveData<Customer> customerLiveData) {
        fireStoreSource.getCustomerObjectByUsername(username, customerLiveData);
    }

    public void registerUser(String username, String password, String fullName,
                             String gender, String birthDay, String phoneNumber,
                             String address, String email, RegisterCallback registerCallback) {
        fireStoreSource.registerUser(username, password, fullName,
                gender, birthDay, phoneNumber, address, email, registerCallback);
    }
}
