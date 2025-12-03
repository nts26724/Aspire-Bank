package com.example.app.ui.bookticket;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.model.FlightOfferRequest.Data;
import com.example.app.data.model.FlightOfferRequest.Passenger;
import com.example.app.data.model.FlightOfferRequest.Slice;
import com.example.app.data.model.FlightOfferRequest;
import com.example.app.data.model.FlightOfferResponse;
import com.example.app.data.model.FlightOfferResponse.Offer;
import com.example.app.data.repository.BookTicketRepository;
import com.example.app.data.repository.RoomRepository;
import com.example.app.interfaces.LoginCallback;
import com.example.app.ui.bookroom.BookRoomViewModel;
import com.example.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookTicketViewModel extends AndroidViewModel {
    private BookTicketRepository bookTicketRepository;
    private MutableLiveData<List<Offer>> listFlightOfferLiveData;
    private MutableLiveData<String> nameCustomerLiveData;
    private MutableLiveData<Boolean> bookingLiveData;
    private SessionManager sessionManager;




    public BookTicketViewModel(@NonNull Application application) {
        super(application);

        bookTicketRepository = new BookTicketRepository();
        sessionManager = SessionManager.getInstance(application);
        listFlightOfferLiveData = new MutableLiveData<>();
        nameCustomerLiveData = new MutableLiveData<>();
        bookingLiveData = new MutableLiveData<>();
    }

    public void getFlightOffers(String origin, String destination,
                                 String departureDate, String quantityAdult,
                                String quantityChildren) {

        List<Slice> slices = new ArrayList<>() {};
        slices.add(new Slice(origin, destination, departureDate));

        List<Passenger> passengers = new ArrayList<>();
        for(int i = 0; i < Integer.parseInt(quantityAdult); i++) {
            passengers.add(new Passenger("adult"));
        }

        for(int i = 0; i < Integer.parseInt(quantityChildren); i++) {
            passengers.add(new Passenger("child"));
        }

        FlightOfferRequest flightOfferRequest
                = new FlightOfferRequest(
                        new Data(
                                slices,
                                passengers,
                                0
                        )
        );

        Log.d("getFlightOffers", "onResponse: out");

        Log.d("flightOfferRequest", flightOfferRequest.getData().getPassengers().get(0).getType() + "");

        bookTicketRepository.getFlightOffers(
                flightOfferRequest,
            new Callback<FlightOfferResponse>() {
                @Override
                public void onResponse(Call<FlightOfferResponse> call, Response<FlightOfferResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("getFlightOffers", "onResponse: ");
                        listFlightOfferLiveData.postValue(response.body().getData().getOffers());
                        return;
                    }
                    Log.d("getFlightOffers", response.isSuccessful() + "");
                    Log.d("getFlightOffers", "onResponse: null");
                    listFlightOfferLiveData.postValue(null);
                }

                @Override
                public void onFailure(Call<FlightOfferResponse> call, Throwable t) {
                    Log.d("getFlightOffers", "onFailure: failure");
                    Log.d("getFlightOffers", "onFailure: " + call.toString() + " " + t);
                    listFlightOfferLiveData.postValue(null);
                }
            }
        );
    }


    public MutableLiveData<List<Offer>> getListFlightOfferLiveData() {
        return listFlightOfferLiveData;
    }


    public void getNameCustomerByUsername(String username) {
        bookTicketRepository.getNameCustomerByUsername(username, nameCustomerLiveData);
    }


    public MutableLiveData<String> getNameCustomerLiveData() {
        return nameCustomerLiveData;
    }

    public void pay(String nameAirline, long price) {
        bookTicketRepository.getAccountByUsername(
                sessionManager.getAccount().getUsername(),
                new LoginCallback() {
                    @Override
                    public void onSuccess(Account account) {
                        if(account.getBalance() < price) {
                            bookingLiveData.postValue(false);
                        } else {
                            bookTicketRepository.widthraw(
                                    SessionManager.getInstance(getApplication()).getAccount().getUsername(),
                                    price);

                            sessionManager.getAccount().widthraw(price);

                            bookTicketRepository.addTransaction(price, "BookRoom Payment", true,
                                    sessionManager.getAccount().getUsername(), nameAirline);

                            bookingLiveData.postValue(true);
                        }
                    }
                    @Override
                    public void onFailure() {
                        Log.d("getAccountByUsername at ViewModel", "onFailure");

                    }
                });


    }

    public MutableLiveData<Boolean> getBookingLiveData() {
        return bookingLiveData;
    }
}
