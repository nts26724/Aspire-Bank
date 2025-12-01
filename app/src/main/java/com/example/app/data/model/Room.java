package com.example.app.data.model;

import androidx.annotation.NonNull;

public class Room {
    @NonNull
    private String id;
    private String name;
    private double rating;
    private double distance;
    private double price;



    public Room(String id, String name, double rating, double distance, double price) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.distance = distance;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }


    public void setPrice(double price) {
        this.price = price;
    }
    public double getPrice() {
        return price;
    }
}






