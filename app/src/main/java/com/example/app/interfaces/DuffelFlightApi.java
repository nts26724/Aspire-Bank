package com.example.app.interfaces;

import com.example.app.data.model.FlightOfferRequest;
import com.example.app.data.model.FlightOfferResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DuffelFlightApi {
    @POST("air/offer_requests")
    Call<FlightOfferResponse> getFlightOffers(
            @Header("Authorization") String token,
            @Header("Duffel-Version") String duffelVersion,
            @Body FlightOfferRequest request
    );
}
