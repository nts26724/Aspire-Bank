package com.example.app.ui.register;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.model.Customer;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.RegisterCallback;

public class RegisterViewModel extends AndroidViewModel {

    private FireStoreSource fireStoreSource;
    private MutableLiveData<Boolean> registerResult;
    private MutableLiveData<String> errorMessage;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        fireStoreSource = new FireStoreSource();
        registerResult = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getRegisterResult() {
        return registerResult;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void register(String username, String password, String confirmPassword,
                         String fullName, String phone, String email, String address, String dob, String gender) {

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(fullName)) {
            errorMessage.setValue("Vui lòng điền đầy đủ thông tin!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessage.setValue("Mật khẩu xác nhận không khớp!");
            return;
        }

        Account newAccount = new Account(username, password, "customer");
        Customer newCustomer = new Customer(
                username,
                fullName,
                dob,
                address,
                phone,
                email,
                gender
        );

        fireStoreSource.registerUser(newAccount, newCustomer, new RegisterCallback() {
            @Override
            public void onSuccess() {
                registerResult.postValue(true);
            }

            @Override
            public void onError(String msg) {
                errorMessage.postValue(msg);
                registerResult.postValue(false);
            }
        });
    }
}