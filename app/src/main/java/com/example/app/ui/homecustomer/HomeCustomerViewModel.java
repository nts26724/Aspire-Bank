package com.example.app.ui.homecustomer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.repository.AccountRepository;
import com.example.app.data.repository.CustomerRepository;
import com.example.app.interfaces.HomeCustomerCallback;

public class HomeCustomerViewModel extends AndroidViewModel {
    private MutableLiveData<Account> accountLiveData;
    private MutableLiveData<String> fullNameLiveData;
    private CustomerRepository customerRepository;

    public HomeCustomerViewModel(@NonNull Application application) {
        super(application);
        accountLiveData = new MutableLiveData<>();
        fullNameLiveData = new MutableLiveData<>();
        customerRepository = new CustomerRepository();
    }


    public void getFullNameByUsername(String username) {
        customerRepository.getFullNameByUsername(username, new HomeCustomerCallback() {
            @Override
            public void onSuccess(String fullName){
                fullNameLiveData.postValue(fullName);
            }

            @Override
            public void onFailure() {
                fullNameLiveData.postValue(null);
            }
        });
    }


    public MutableLiveData<Account> getAccountLiveData() {
        return accountLiveData;
    }

    public void setAccountLiveData(Account account) {
        accountLiveData.postValue(account);
    }


    public MutableLiveData<String> getFullNameLiveData() {
        return fullNameLiveData;
    }

    public void setFullNameLiveData(MutableLiveData<String> fullNameLiveData) {
        this.fullNameLiveData = fullNameLiveData;
    }
}
