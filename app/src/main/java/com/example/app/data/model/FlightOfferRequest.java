package com.example.app.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FlightOfferRequest {

    @SerializedName("data")
    private Data data;

    public FlightOfferRequest(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("slices")
        private List<Slice> slices;

        @SerializedName("passengers")
        private List<Passenger> passengers;

        @SerializedName("max_connections")
        private int maxConnections;

        public Data(List<Slice> slices, List<Passenger> passengers, int maxConnections) {
            this.slices = slices;
            this.passengers = passengers;
            this.maxConnections = maxConnections;
        }

        public List<Passenger> getPassengers() {
            return passengers;
        }
    }

    public static class Slice {

        @SerializedName("origin")
        private String origin;

        @SerializedName("destination")
        private String destination;

        @SerializedName("departure_date")
        private String departureDate;

        public Slice(String origin, String destination, String departureDate) {
            this.origin = origin;
            this.destination = destination;
            this.departureDate = departureDate;
        }
    }

    public static class Passenger {

        @SerializedName("type")
        private String type;

        public Passenger(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public Data getData() {
        return data;
    }
}
