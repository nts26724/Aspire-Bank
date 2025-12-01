package com.example.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotelList {
    @SerializedName("data")
    private List<Hotel> data;

    public List<Hotel> getData() {
        return data;
    }
}
