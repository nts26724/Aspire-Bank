package com.example.app.data.remote;

import android.util.Log;

import com.example.app.BuildConfig;
import com.example.app.data.model.AccessTokenResponse;
import com.example.app.data.model.BookingRequest;
import com.example.app.data.model.BookingResponse;
import com.example.app.data.model.HotelList;
import com.example.app.data.model.OfferList;
import com.example.app.interfaces.AmadeusAuthApi;
import com.example.app.interfaces.AmadeusHotelApi;
import com.example.app.interfaces.TokenCallback;
import com.example.app.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Amadeus  {
    private static Amadeus instance;
    private String accessToken;
    private long expiresIn;
    private AmadeusAuthApi amadeusAuthApi;
    private AmadeusHotelApi amadeusHotelApi;


    public Amadeus() {
        Retrofit retrofit = RetrofitClient.getInstance();

        amadeusAuthApi = retrofit.create(AmadeusAuthApi.class);
        amadeusHotelApi = retrofit.create(AmadeusHotelApi.class);
    }

    public static Amadeus getInstance() {
        if (instance == null) {
            instance = new Amadeus();
        }
        return instance;
    }

    public void getAccessToken(TokenCallback tokenCallback) {
        long now = System.currentTimeMillis();
        if (accessToken == null || now > expiresIn) {
            getAccessTokenFromApi(tokenCallback);
            return;
        }

        tokenCallback.onSuccess(accessToken);
    }


    public void getAccessTokenFromApi(TokenCallback tokenCallback) {

        amadeusAuthApi.getAccessToken(
                "client_credentials",
                BuildConfig.AMADEUS_API_KEY,
                BuildConfig.AMADEUS_API_SECRET
        ).enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    AccessTokenResponse res = response.body();

                    accessToken = "Bearer " + res.getAccessToken();
                    expiresIn = System.currentTimeMillis()
                            + (res.getExpiresIn() * 1000);

                    Log.d("getAccessTokenFromApi", response.isSuccessful() + "");
                    tokenCallback.onSuccess(accessToken);
                } else {
                    Log.d("getAccessToken",
                            "responeOsSuccessful: " + response.isSuccessful() +
                            " responeBody != null: " + (response.body() != null)
                    );
                }
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                Log.d("getAccessToken", "Fail");
                tokenCallback.onFailure();
            }
        });

    }


    public void getHotelsByGeoCode(double latitude, double longitude,
                                   int radius, Callback<HotelList> callback) {

        Log.d("latitude", latitude + "");
        Log.d("longitude", longitude + "");
        Log.d("radius", radius + "");

        getAccessToken(new TokenCallback() {
            @Override
            public void onSuccess(String accessToken) {

                amadeusHotelApi.getHotelsByGeoCode(
                        accessToken,
                        latitude,
                        longitude,
                        radius
                ).enqueue(new Callback<HotelList>() {
                    @Override
                    public void onResponse(Call<HotelList> call, Response<HotelList> response) {
                        Log.d("getHotelsByGeoCode at Amadeus", response.isSuccessful() + "");
                        callback.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(Call<HotelList> call, Throwable t) {
                        Log.d("getHotelsByGeoCode", "fail");
                        callback.onFailure(call, t);
                    }
                });
            }

            @Override
            public void onFailure() {
                Log.d("getAccessToken", "fail");
            }
        });

    }


    public void getMultiHotelOffers(String hotelIds, int adults,
                                    String checkInDate, String checkOutDate,
                                    int roomQuantity, Callback<OfferList> callback) {

        Log.d("getMultiHotelOffers in amadeus", "getMultiHotelOffers: ");

        getAccessToken(new TokenCallback() {
            @Override
            public void onSuccess(String accessToken) {

                Log.d("getMultiHotelOffers", "onSuccess");

                amadeusHotelApi.getMultiHotelOffers(
                        accessToken,
                        hotelIds,
                        adults,
                        checkInDate,
                        checkOutDate,
                        roomQuantity
                ).enqueue(new Callback<OfferList>() {
                    @Override
                    public void onResponse(Call<OfferList> call, Response<OfferList> response) {
                        Log.d("getMultiHotelOffers", response.isSuccessful() + "");
                        callback.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(Call<OfferList> call, Throwable t) {
                        Log.d("getMultiHotelOffers", "fail");
                        callback.onFailure(call, t);
                    }
                });
            }

            @Override
            public void onFailure() {
                Log.d("getAccessToken", "fail");
            }
        });
    }


    public void bookRoom(String offerId, Callback<BookingResponse> callback) {

        getAccessToken(new TokenCallback() {
            @Override
            public void onSuccess(String accessToken) {
                BookingRequest request = new BookingRequest(offerId);
                Log.e("TOKEN_SENT_TO_BOOKING", "[" + accessToken + "]");

                amadeusHotelApi.bookHotel(accessToken, request)
                        .enqueue(new Callback<BookingResponse>() {
                            @Override
                            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                                Log.d("BOOKING_RESPONSE", "Success: " + response.isSuccessful());

                                if (response.isSuccessful() && response.body() != null) {
                                    BookingResponse.Data data = response.body().getData();
                                    Log.d("BOOKING_RESULT",
                                            "BookingId: " + data.getId() +
                                                    " Status: " + data.getStatus());

                                    callback.onResponse(call, response);
                                } else {
                                    try {
                                        Log.e("BOOKING_ERROR", response.errorBody().string());
                                    } catch (Exception e) { }
                                }
                            }

                            @Override
                            public void onFailure(Call<BookingResponse> call, Throwable t) {
                                Log.e("BOOKING_FAIL", t.getMessage());
                            }
                        });
            }

            @Override
            public void onFailure() {
                Log.d("Booking", "onFailure: ");
            }
        });
    }

}
