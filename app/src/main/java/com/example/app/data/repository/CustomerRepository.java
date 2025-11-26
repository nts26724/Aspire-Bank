package com.example.app.data.repository;

import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.HomeCustomerCallback;

public class CustomerRepository {
    private FireStoreSource fireStoreSource;

    public CustomerRepository() {
        fireStoreSource = new FireStoreSource();
    }

    public void getFullNameByUsername(String username, HomeCustomerCallback homeCustomerCallback) {
        fireStoreSource.getCustomerByUsername(username, homeCustomerCallback);
    }
}
