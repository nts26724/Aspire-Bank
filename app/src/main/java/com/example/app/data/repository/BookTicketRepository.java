package com.example.app.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Customer;
import com.example.app.data.model.FlightOfferRequest;
import com.example.app.data.model.FlightOfferResponse;
import com.example.app.data.remote.Duffel;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.HomeCustomerCallback;
import com.example.app.interfaces.LoginCallback;

import retrofit2.Callback;

public class BookTicketRepository {
    private Duffel duffel;
    private FireStoreSource fireStoreSource;


    public BookTicketRepository() {
        duffel = Duffel.getInstance();
        fireStoreSource = new FireStoreSource();
    }

    public void getFlightOffers(FlightOfferRequest flightOfferRequest, Callback<FlightOfferResponse> callback) {
        duffel.getFlightOffers(flightOfferRequest, callback);
    }


    public void getNameCustomerByUsername(String username, MutableLiveData<String> nameCustomerLiveData) {
        fireStoreSource.getCustomerByUsername(username, new HomeCustomerCallback() {
            @Override
            public void onSuccess(String fullName) {
                nameCustomerLiveData.postValue(fullName);
            }
            @Override
            public void onFailure() {
                nameCustomerLiveData.postValue(null);
            }
        });
    }


    public void getAccountByUsername(String username, LoginCallback callback) {
        fireStoreSource.getAccountByUsername(username, callback);
    }


    public void widthraw(String username, long price) {
        fireStoreSource.widthraw(username, price);
    }

    public void addTransaction(long amount, String content, boolean transfer,
                               String usernameTransfer, String usernameReceive) {
        fireStoreSource.addTransaction(amount, content, transfer,
                usernameTransfer, usernameReceive);
    }
}
