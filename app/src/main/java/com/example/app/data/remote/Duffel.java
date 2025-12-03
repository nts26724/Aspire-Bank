package com.example.app.data.remote;

import com.example.app.BuildConfig;
import com.example.app.data.model.FlightOfferRequest;
import com.example.app.data.model.FlightOfferResponse;
import com.example.app.interfaces.DuffelFlightApi;
import com.example.app.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Duffel {
    private static Duffel insance;
    private DuffelFlightApi duffelFlightApi;

    public Duffel() {
        Retrofit retrofit = RetrofitClient.getInstanceDuffel();

        duffelFlightApi = retrofit.create(DuffelFlightApi.class);
    }


    public static Duffel getInstance() {
        if (insance == null) {
            insance = new Duffel();
        }
        return insance;
    }


    public void getFlightOffers(FlightOfferRequest flightOfferRequest, Callback<FlightOfferResponse> callback) {
        duffelFlightApi.getFlightOffers(
                "Bearer " + BuildConfig.DUFFEL_API_KEY,
                "v2",
                flightOfferRequest
        ).enqueue(new Callback<FlightOfferResponse>() {
            @Override
            public void onResponse(Call<FlightOfferResponse> call, Response<FlightOfferResponse> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<FlightOfferResponse> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
