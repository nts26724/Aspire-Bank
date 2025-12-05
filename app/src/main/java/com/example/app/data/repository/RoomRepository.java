package com.example.app.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.BookingResponse;
import com.example.app.data.model.HotelList;
import com.example.app.data.model.OfferList;
import com.example.app.data.remote.Amadeus;
import com.example.app.data.remote.FireStoreSource;
import com.example.app.interfaces.HomeCustomerCallback;
import com.example.app.interfaces.LoginCallback;

import retrofit2.Callback;

public class RoomRepository {
    private Amadeus amadeus;
    private FireStoreSource fireStoreSource;

    public RoomRepository() {
        amadeus = Amadeus.getInstance();
        fireStoreSource = new FireStoreSource();
    }

    public void getHotelsByGeoCode(double latitude, double longitude,
                                   int radius, Callback<HotelList> callback) {
        amadeus.getHotelsByGeoCode(latitude, longitude, radius, callback);
    }

    public void getMultiHotelOffers(String hotelIds, int adults,
                                    String checkInDate, String checkOutDate,
                                    int roomQuantity, Callback<OfferList> callback) {

        amadeus.getMultiHotelOffers(hotelIds, adults, checkInDate,
                checkOutDate, roomQuantity, callback);
    }

    public void booking(String offerId, Callback<BookingResponse> callback) {
        amadeus.bookRoom(offerId, callback);
    }

    public void widthraw(String username, long price) {
        fireStoreSource.widthraw(username, price);
    }

    public void getAccountByUsername(String username, LoginCallback callback) {
        fireStoreSource.getAccountByUsername(username, callback);
    }

    public void addTransaction(long amount, String content, boolean transfer,
                               String usernameTransfer, String usernameReceive) {
        fireStoreSource.addTransaction(amount, content, transfer,
                usernameTransfer, usernameReceive);
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
}
