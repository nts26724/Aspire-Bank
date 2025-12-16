package com.example.app.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.repository.AccountRepository;
import com.example.app.interfaces.LoginCallback;
import com.example.app.utils.Result;
import com.example.app.utils.SessionManager;

public class LoginViewModel extends AndroidViewModel {
    private AccountRepository accountRepository;
    private MutableLiveData<Result> loginResult;
    private SessionManager sessionManager;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository();
        loginResult = new MutableLiveData<>();
        sessionManager = new SessionManager(application.getApplicationContext());
    }

    public void login(String username, String passwordText) {
        accountRepository.getUserByUsername(username, new LoginCallback() {
            @Override
            public void onSuccess(Account account) {
                if (account == null) {
                    loginResult.postValue(Result.NOT_FOUND);
                    return;
                }

                if (account.getPassword() == null) {
                    loginResult.postValue(Result.ERROR);
                    return;
                }

                if (!account.getPassword().equals(passwordText)) {
                    loginResult.postValue(Result.WRONG_PASSWORD);
                    return;
                }

                sessionManager.setAccount(account);

                if ("customer".equals(account.getRole())) {
                    loginResult.postValue(Result.CUSTOMER);
                } else {
                    loginResult.postValue(Result.OFFICER);
                }
            }

            @Override
            public void onFailure() {
                loginResult.postValue(Result.ERROR);
            }
        });
    }

    public MutableLiveData<Result> getLoginResult() {
        return loginResult;
    }
}