package com.example.app.ui.receiptpayment;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Receipt;
import com.example.app.data.repository.ReceiptPaymentRepository;
import com.example.app.interfaces.ReceiptPaymentCallback;

import java.util.List;

public class ReceiptPaymentViewModel extends AndroidViewModel {
    private ReceiptPaymentRepository receiptPaymentRepository;
    private MutableLiveData<List<Receipt>> listReceipts;


    public ReceiptPaymentViewModel(@NonNull Application application) {
        super(application);
        receiptPaymentRepository = new ReceiptPaymentRepository();
        listReceipts = new MutableLiveData<>();
    }

    public void getReceiptsByUsername(String username) {
        receiptPaymentRepository.getReceiptsByUsername(username, new ReceiptPaymentCallback() {
            @Override
            public void onSuccess(List<Receipt> receipts) {
                listReceipts.postValue(receipts);
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

    public MutableLiveData<List<Receipt>> getListReceipts() {
        return listReceipts;
    }
}
