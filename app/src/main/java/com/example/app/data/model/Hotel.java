package com.example.app.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Hotel {

    public static class Distance {
        @SerializedName("value")
        private double value;

        public double getValue() {
            return value;
        }
    }

    @NonNull
    @SerializedName("hotelId")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("distance")
    private Distance distance;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDistance() {
        return distance != null ? distance.getValue() : 0;
    }
}
