package com.example.app.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl("https://test.api.amadeus.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }

//    OkHttpClient client = new OkHttpClient.Builder()
//            .connectTimeout(15, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .build();
//
//    instance = new Retrofit.Builder()
//            .baseUrl("https://test.api.amadeus.com/")
//        .client(client)
//        .addConverterFactory(GsonConverterFactory.create())
//            .build();

}
