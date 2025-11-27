package com.example.app.ui.notification;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Transaction;
import com.example.app.data.repository.TransactionRepository;
import com.example.app.interfaces.TransactionCallback;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {
    private TransactionRepository transactionRepository;
    private MutableLiveData<List<Transaction>> listNotification;


    public NotificationViewModel(@NonNull Application application) {
        super(application);
        transactionRepository = new TransactionRepository();
        listNotification = new MutableLiveData<>();
    }

    public void getTransactionsByUsername(String username) {
        transactionRepository.getTransactionsByUsername(username, new TransactionCallback() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                listNotification.postValue(transactions);
            }

            @Override
            public void onFailure() {
                Toast.makeText(getApplication(),
                        "Không thể kết nối tới máy chủ",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    public MutableLiveData<List<Transaction>> getListNotification() {
        return listNotification;
    }
}
