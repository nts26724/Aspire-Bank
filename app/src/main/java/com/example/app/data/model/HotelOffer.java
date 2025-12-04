package com.example.app.data.model;

import androidx.annotation.NonNull;

// co the xoa
public class HotelOffer {
    @NonNull
    private String id;
    // offerId
    private String name;
    private int bed;
    private double distance;
    private String price;

    public HotelOffer(@NonNull String id, String name, int bed, double distance, String price) {
        this.id = id;
        this.name = name;
        this.bed = bed;
        this.distance = distance;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getBed() {
        return bed;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
