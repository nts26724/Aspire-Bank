package com.example.app.ui.verify;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.repository.BookTicketRepository;
import com.example.app.data.repository.VerifyRepository;
import com.example.app.interfaces.PhoneNumberCallBack;
import com.example.app.utils.SessionManager;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class VerifyViewModel extends AndroidViewModel {
    private VerifyRepository verifyRepository;
    private MutableLiveData<String> phoneNumberLiveData;
    private MutableLiveData<Boolean> verifyLiveData;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;




    public VerifyViewModel(@NonNull Application application) {
        super(application);

        verifyRepository = new VerifyRepository();
        phoneNumberLiveData = new MutableLiveData<>();
        verifyLiveData = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
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
        verifyRepository.getPhoneNumberByUsername(username, new PhoneNumberCallBack() {
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


    public void sendOTP(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(((VerifyActivity) getApplication().getApplicationContext()))
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    public void verifyOTP(String userOTP) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, userOTP);

        signInWithCredential(credential);
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, task -> {
                    verifyLiveData.postValue(true);
                });
    }
}
