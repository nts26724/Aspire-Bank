package com.example.app.ui.listcustomer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.model.Customer;
import com.example.app.data.repository.ListCustomerRepository;
import com.example.app.interfaces.HomeCustomerCallback;
import com.example.app.interfaces.LoginCallback;
import com.example.app.interfaces.RegisterCallback;
import com.example.app.utils.SessionManager;

import java.util.List;

public class ListCustomerViewModel extends AndroidViewModel {
    private ListCustomerRepository listCustomerRepository;
    private MutableLiveData<List<Account>> listCustomerLiveData;
    private MutableLiveData<Customer> customerByUsernameLiveData;
    private MutableLiveData<Boolean> isUpdateCustomerSuccess;
    private MutableLiveData<Boolean> isRegisterUserSuccess;
    private MutableLiveData<Boolean> isExistUsername;
    public ListCustomerViewModel(@NonNull Application application) {
        super(application);

        listCustomerRepository = new ListCustomerRepository();
        listCustomerLiveData = new MutableLiveData<>();
        customerByUsernameLiveData = new MutableLiveData<>();
        isUpdateCustomerSuccess = new MutableLiveData<>();
        isRegisterUserSuccess = new MutableLiveData<>();
        isExistUsername = new MutableLiveData<>();
    }

    public void getListCustomer() {
        listCustomerRepository.getListCustomer(listCustomerLiveData);
    }

    public MutableLiveData<List<Account>> getListCustomerLiveData() {
        return listCustomerLiveData;
    }

    public void updateCustomer(String username, String fullName,
                              String birthDay, String phoneNumber,
                              String address, String email, String gender) {
        listCustomerRepository.updateCustomer(username, fullName, birthDay,
                phoneNumber, address, email, gender, isUpdateCustomerSuccess);
    }


    public MutableLiveData<Boolean> isUpdateCustomerSuccess() {
        return isUpdateCustomerSuccess;
    }

    public MutableLiveData<Customer> getCustomerByUsernameLiveData() {
        return customerByUsernameLiveData;
    }

    public void getCustomerByUsername(String username) {
        listCustomerRepository.getCustomerByUsername(username, customerByUsernameLiveData);
    }

    public void registerUser(String username, String password, String fullName,
                             String gender, String birthDay, String phoneNumber,
                             String address, String email) {
        listCustomerRepository.registerUser(
                new Account(username, password, "customer"),
                new Customer(username, fullName, birthDay, address, phoneNumber, email, gender),
                new RegisterCallback() {
                    @Override
                    public void onSuccess() {
                        isRegisterUserSuccess.postValue(true);
                    }

                    @Override
                    public void onError(String msg) {
                        isRegisterUserSuccess.postValue(false);
                    }
                });
    }

    public MutableLiveData<Boolean> isRegisterUserSuccess() {
        return isRegisterUserSuccess;
    }


    public void isExistUsername(String username) {
        listCustomerRepository.isExistUsername(username, new LoginCallback() {
            @Override
            public void onSuccess(Account account) {
                if (account == null) {
                    isExistUsername.postValue(false);
                    return;
                }
                isExistUsername.postValue(true);
            }

            @Override
            public void onFailure() {
                isExistUsername.postValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> isExistUsername() {
        return isExistUsername;
    }
}
