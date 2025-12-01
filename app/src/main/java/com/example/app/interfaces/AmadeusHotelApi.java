package com.example.app.interfaces;

import com.example.app.data.model.BookingRequest;
import com.example.app.data.model.BookingResponse;
import com.example.app.data.model.Hotel;
import com.example.app.data.model.HotelList;
import com.example.app.data.model.Offer;
import com.example.app.data.model.OfferList;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface AmadeusHotelApi {
    @GET("v1/reference-data/locations/hotels/by-geocode")
    Call<HotelList> getHotelsByGeoCode(
            @Header("Authorization") String token,
            @Query("latitude") double latitude ,
            @Query("longitude") double longitude ,
            @Query("radius") int radius
    );


    @GET("v3/shopping/hotel-offers")
    Call<OfferList> getMultiHotelOffers(
            @Header("Authorization") String token,
            @Query("hotelIds") String hotelIds,
            @Query("adults") int adults,
            @Query("checkInDate") String checkInDate,
            @Query("checkOutDate") String checkOutDate,
            @Query("roomQuantity") int roomQuantity
    );


    @POST("v1/booking/hotel-bookings")
    Call<BookingResponse> bookHotel(
            @Header("Authorization") String token,
            @Body BookingRequest request
    );
}
