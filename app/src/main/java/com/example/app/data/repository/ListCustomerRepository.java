package com.example.app.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.model.Customer;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.HomeCustomerCallback;
import com.example.app.interfaces.LoginCallback;
import com.example.app.interfaces.RegisterCallback;

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

    public void registerUser(Account account, Customer customer, RegisterCallback registerCallback) {
        fireStoreSource.registerUser(account, customer, registerCallback);
    }

    public void isExistUsername(String username, LoginCallback loginCallback) {
        fireStoreSource.getAccountByUsername(username, loginCallback);
    }
}
