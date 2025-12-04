package com.example.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OfferList {
    @SerializedName("data")
    private List<Offer> data;

    public List<Offer> getData() {
        return data;
    }
}

