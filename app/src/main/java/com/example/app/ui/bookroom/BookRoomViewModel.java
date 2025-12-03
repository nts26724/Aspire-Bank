package com.example.app.ui.bookroom;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app.data.model.Account;
import com.example.app.data.model.BookingResponse;
import com.example.app.data.model.Hotel;
import com.example.app.data.model.HotelList;
import com.example.app.data.model.HotelOffer;
import com.example.app.data.model.Offer;
import com.example.app.data.model.OfferList;
import com.example.app.data.repository.RoomRepository;
import com.example.app.interfaces.LoginCallback;
import com.example.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRoomViewModel extends AndroidViewModel {
    private RoomRepository roomRepository;
    private MutableLiveData<List<HotelOffer>> listHotelOfferLiveData;
    private MutableLiveData<Boolean> bookingLiveData;
    private SessionManager sessionManager;
    private MutableLiveData<String> nameCustomerLiveData;



    public BookRoomViewModel(@NonNull Application application) {
        super(application);
        sessionManager = SessionManager.getInstance(application);
        listHotelOfferLiveData = new MutableLiveData<>();
        roomRepository = new RoomRepository();
        bookingLiveData = new MutableLiveData<>();
    }


    public void getListHotelOffer(String latitude, String longitude,
                                  String radius, String quantityPeople,
                                  String checkInDate, String checkOutDate,
                                  String quantityRoom) {

        roomRepository.getHotelsByGeoCode(
                Double.parseDouble(latitude),
                Double.parseDouble(longitude),
                Integer.parseInt(radius),
                new Callback<HotelList>() {
                    @Override
                    public void onResponse(Call<HotelList> call, Response<HotelList> response) {
                        Log.d("responseBody", response.body() + "");
                        Log.d("response.isSuccessful", response.isSuccessful() + "");

                        if(response.isSuccessful() &&
                                response.body() != null &&
                                response.body().getData() != null &&
                                !response.body().getData().isEmpty()) {

                            HotelList hotelList = response.body();
                            List<String> listHotelID = new ArrayList<>();

                            for (Hotel hotel : hotelList.getData()) {
                                String id = hotel.getId();
                                listHotelID.add(id);
                            }

                            Log.d("getHotelsByGeoCode at ViewModel",
                                    (response.isSuccessful()) + " " +
                                            (response.body() != null));

                            getMultiHotelOffers(
                                    TextUtils.join(",", listHotelID),
                                    quantityPeople,
                                    checkInDate,
                                    checkOutDate,
                                    quantityRoom,
                                    hotelList.getData()
                            );
                        } else {
                            Log.d("getHotelsByGeoCode else",
                                    response.isSuccessful() + ""
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<HotelList> call, Throwable t) {
                        Log.d("getHotelsByGeoCode", "fail");
                    }
                }
        );
    }


    public void getMultiHotelOffers(String hotelIds, String quantityPeople,
                                    String checkInDate, String checkOutDate,
                                    String quantityRoom, List<Hotel> listHotel) {

        Log.d("getMultiHotelOffers", "getMultiHotelOffers: ");

        roomRepository.getMultiHotelOffers(
                hotelIds,
                Integer.parseInt(quantityPeople),
                checkInDate,
                checkOutDate,
                Integer.parseInt(quantityRoom),
                new Callback<OfferList>() {
                    @Override
                    public void onResponse(Call<OfferList> call, Response<OfferList> response) {
                        if (response.isSuccessful() &&
                                response.body() != null &&
                                response.body().getData() != null &&
                                !response.body().getData().isEmpty()) {

                            OfferList offerList = response.body();
                            List<Offer> listOffer = offerList.getData();
                            List<HotelOffer> listHotelOffer = new ArrayList<>();
                            Map<String, Hotel> hotelMap = new HashMap<>();

                            for (Hotel hotel : listHotel) {
                                hotelMap.put(hotel.getId(), hotel);
                            }

                            for(Offer currOffer : listOffer) {
                                if(!currOffer.isAvailable())
                                    continue;

                                Hotel currHotel = hotelMap.get(
                                        currOffer.getHotelId());

                                if (currHotel == null)
                                    continue;

                                for(int i = 0; i < currOffer.getOffers().size(); i++) {
                                    listHotelOffer.add(new HotelOffer(
                                        currOffer.getOfferId(i),
                                        currHotel.getName(),
                                        currOffer.getBed(i),
                                        currHotel.getDistance(),
                                        currOffer.getPrice(i)
                                    ));
                                }
                            }

                            Log.d("getMultiHotelOffers", response.isSuccessful() + "");
                            listHotelOfferLiveData.postValue(listHotelOffer);
                        } else {
                            Log.d("getMultiHotelOffers", response.isSuccessful() + "");
                            listHotelOfferLiveData.postValue(new ArrayList<>());
                        }

                    }

                    @Override
                    public void onFailure(Call<OfferList> call, Throwable t) {
                        Log.d("getMultiHotelOffers", "fail");
                        listHotelOfferLiveData.postValue(null);
                    }
                }
        );
    }


    public MutableLiveData<List<HotelOffer>> getListHotelOfferLiveData() {
        return listHotelOfferLiveData;
    }


    public void booking(String offerId) {
        roomRepository.booking(offerId, new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                bookingLiveData.postValue(true);
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                bookingLiveData.postValue(false);
            }
        });
    }


    public void pay(String hotelName, String offerId, long price) {
        roomRepository.getAccountByUsername(
                sessionManager.getAccount().getUsername(),
                new LoginCallback() {
            @Override
            public void onSuccess(Account account) {
                Log.d("getAccountByUsername at ViewModel", "onSuccess: ");
                if(account.getBalance() < price) {
                    bookingLiveData.postValue(false);
                } else {
                    roomRepository.widthraw(
                            sessionManager.getAccount().getUsername(),
                            price);

                    sessionManager.getAccount().widthraw(price);

                    roomRepository.addTransaction(price, "BookRoom Payment", true,
                            sessionManager.getAccount().getUsername(), hotelName);

//                    booking(offerId);
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


    public void getNameCustomerByUsername(String username) {
        roomRepository.getNameCustomerByUsername(username, nameCustomerLiveData);
    }

    public MutableLiveData<String> getNameCustomerLiveData() {
        return nameCustomerLiveData;
    }

}
