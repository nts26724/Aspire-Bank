package com.example.app.ui.login;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.repository.AccountRepository;
import com.example.app.interfaces.LoginCallback;
import com.example.app.ui.homecustomer.HomeCustomerActivity;
import com.example.app.utils.Result;
import com.example.app.utils.SessionManager;

public class LoginViewModel extends AndroidViewModel {
    private AccountRepository accountRepository;
    public MutableLiveData<Result> loginResult;
//    private MutableLiveData<Account> accountLiveData;



    public LoginViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository();
        loginResult = new MutableLiveData<>();
//        accountLiveData = new MutableLiveData<>();
    }

    public void login(String username, String passwordText) {
        accountRepository.getUserByUsername(username,new LoginCallback() {
            @Override
            public void onSuccess(Account account) {
                if (account == null) {
                    loginResult.postValue(Result.NOT_FOUND);
                } else if(!account.getPassword().equals(passwordText)){
                    loginResult.postValue(Result.WRONG_PASSWORD);
                } else if(account.getRole().equals("customer")){
                    SessionManager.getInstance().setUser(account);
//                    accountLiveData.postValue(account);
                    loginResult.postValue(Result.CUSTOMER);
                } else {
                    SessionManager.getInstance().setUser(account);
//                    accountLiveData.postValue(account);
                    loginResult.postValue(Result.OFFICER);
                }
            }

            @Override
            public void onFailure() {
                loginResult.postValue(Result.ERROR);
            }
        });
    }

}
