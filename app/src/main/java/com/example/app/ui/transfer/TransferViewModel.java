package com.example.app.ui.transfer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app.data.model.Account;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.LoginCallback;
import com.example.app.interfaces.ReceiverCallback;

public class TransferViewModel extends ViewModel {

    private final FireStoreSource fireStoreSource;
    private final MutableLiveData<String> receiverName = new MutableLiveData<>();
    private final MutableLiveData<String> receiverBankName = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Double> currentBalance = new MutableLiveData<>();

    public TransferViewModel() {
        this.fireStoreSource = new FireStoreSource();
    }

    public void findReceiverByCardNumber(String cardNumber, boolean isInternal, String bankName) {
        if (!isInternal) {
            if (cardNumber.length() >= 6) {
                receiverName.postValue("NGUYEN VAN B");
                if (bankName != null && !bankName.isEmpty()) {
                    receiverBankName.postValue(bankName.toUpperCase());
                } else {
                    receiverBankName.postValue("KHÁCH HÀNG KHÁC");
                }
                errorMessage.postValue(null);
            } else {
                receiverName.postValue(null);
                receiverBankName.postValue(null);
            }
            return;
        }

        fireStoreSource.getAccountByNumber(cardNumber, new ReceiverCallback() {
            @Override
            public void onSuccess(String name) {
                receiverName.postValue(name);
                receiverBankName.postValue("ASPIRE BANK");
                errorMessage.postValue(null);
            }

            @Override
            public void onFailure(String error) {
                receiverName.postValue(null);
                receiverBankName.postValue(null);
                errorMessage.postValue(error);
            }
        });
    }

    public void fetchCurrentBalance(String myUsername) {
        fireStoreSource.getAccountByUsername(myUsername, new LoginCallback() {
            @Override
            public void onSuccess(Account account) {
                if (account != null) {
                    currentBalance.postValue((double) account.getBalance());
                }
            }
            @Override
            public void onFailure() {
                currentBalance.postValue(0.0);
            }
        });
    }

    public LiveData<String> getReceiverNameLiveData() {
        return receiverName;
    }

    public LiveData<String> getReceiverBankNameLiveData() {
        return receiverBankName;
    }

    public LiveData<String> getErrorMessageLiveData() {
        return errorMessage;
    }

    public LiveData<Double> getCurrentBalanceLiveData() {
        return currentBalance;
    }
}