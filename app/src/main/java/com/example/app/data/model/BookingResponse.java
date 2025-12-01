package com.example.app.data.model;

import com.google.gson.annotations.SerializedName;

public class BookingResponse {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("id")
        private String id;

        @SerializedName("offerId")
        private String offerId;

        @SerializedName("status")
        private String status;

        public String getId() { return id; }
        public String getOfferId() { return offerId; }
        public String getStatus() { return status; }
    }
}
