package com.example.app.ui.homecustomer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.repository.AccountRepository;

public class HomeCustomerViewModel extends AndroidViewModel {
    private MutableLiveData<Account> accountLiveData;

    public HomeCustomerViewModel(@NonNull Application application) {
        super(application);
        accountLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Account> getAccountLiveData() {
        return accountLiveData;
    }

    public void setAccountLiveData(Account account) {
        accountLiveData.postValue(account);
    }
}
