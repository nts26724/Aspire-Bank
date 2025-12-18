package com.example.app.ui.verify;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.repository.VerifyOTPRepository;
import com.example.app.interfaces.LoginCallback;
import com.example.app.interfaces.PhoneNumberCallBack;
import com.example.app.utils.SessionManager;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VerifyOTPViewModel extends AndroidViewModel {
    private VerifyOTPRepository verifyOTPRepository;
    private MutableLiveData<String> phoneNumberLiveData;
    private MutableLiveData<Boolean> verifyLiveData;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private SessionManager sessionManager;
    private MutableLiveData<Boolean> booleanLiveData;
    private MutableLiveData<Boolean> depositLiveData;




    public VerifyOTPViewModel(@NonNull Application application) {
        super(application);

        verifyOTPRepository = new VerifyOTPRepository();
        phoneNumberLiveData = new MutableLiveData<>();
        verifyLiveData = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
        sessionManager = SessionManager.getInstance(application);
        booleanLiveData = new MutableLiveData<>();
        depositLiveData = new MutableLiveData<>();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mVerificationId = verificationId;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        };
    }


    public MutableLiveData<Boolean> getVerifyLiveData() {
        return verifyLiveData;
    }


    public MutableLiveData<String> getPhoneNumberLiveData() {
        return phoneNumberLiveData;
    }


    public void getPhoneNumberByUsername(String username) {
        verifyOTPRepository.getPhoneNumberByUsername(username, new PhoneNumberCallBack() {
            @Override
            public void onSuccess(String phoneNumber) {
                phoneNumberLiveData.postValue(phoneNumber);
            }

            @Override
            public void onFailure() {
                phoneNumberLiveData.postValue(null);
            }
        });
    }


    public void sendOTP(String phoneNumber, VerifyOTPActivity activity) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber(phoneNumber)
                        .setPhoneNumber("+16505556789")
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(mCallbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
        Log.d("Mã OTP", "Đã gửi");
        Toast.makeText(getApplication(), "Mã OTP là: 123456", Toast.LENGTH_SHORT).show();
    }


    public void verifyOTP(String userOTP) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, userOTP);

        signInWithCredential(credential);
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Executors.newSingleThreadExecutor(), task -> {
                    verifyLiveData.postValue(task.isSuccessful());
                });
    }


    public void widthraw(String usernameTransfer, long amount, String content, String usernameReceive) {
        verifyOTPRepository.getAccountByUsername(
                usernameTransfer,
                new LoginCallback() {
                    @Override
                    public void onSuccess(Account account) {
                        Log.d("getAccountByUsername at ViewModel", "onSuccess: ");
                        if(account.getBalance() < amount) {
                            booleanLiveData.postValue(false);
                        } else {
                            verifyOTPRepository.widthraw(
                                    sessionManager.getAccount().getUsername(),
                                    amount);

                            sessionManager.getAccount().withdraw(amount);

                            if(usernameReceive.contains("EVN")){
                                deleteReceiptByReceiptID(usernameReceive);
                            }

                            verifyOTPRepository.addTransaction(amount, content, true,
                                    usernameTransfer, usernameReceive);

                            booleanLiveData.postValue(true);
                        }
                    }

                    @Override
                    public void onFailure() {
                        Log.d("getAccountByUsername at ViewModel", "onFailure");
                    }
                });
    }


    public void deleteReceiptByReceiptID(String receiptID) {
        verifyOTPRepository.deleteReceiptByReceiptID(receiptID);
    }


    public MutableLiveData<Boolean> getBooleanLiveData() {
        return booleanLiveData;
    }


    public void deposit(String username, long amount) {
        verifyOTPRepository.deposit(username, amount);
        sessionManager.getAccount().deposit(amount);

        verifyOTPRepository.addTransaction(amount, "Deposit account", false,
                username, sessionManager.getAccount().getUsername());

        depositLiveData.postValue(true);
    }

    public MutableLiveData<Boolean> getDepositLiveData() {
        return depositLiveData;
    }
}
