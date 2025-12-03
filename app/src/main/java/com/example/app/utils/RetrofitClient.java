package com.example.app.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    private static Retrofit instanceDuffel;

    public static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl("https://test.api.amadeus.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }


    public static Retrofit getInstanceDuffel() {
        if (instanceDuffel == null) {
            instanceDuffel = new Retrofit.Builder()
                    .baseUrl("https://api.duffel.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instanceDuffel;
    }
}
