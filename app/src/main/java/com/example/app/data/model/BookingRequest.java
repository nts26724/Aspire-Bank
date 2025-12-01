package com.example.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BookingRequest {
    @SerializedName("data")
    private Data data;

    public BookingRequest(String offerId) {
        data = new Data(offerId);
    }

    public static class Data {
        @SerializedName("offerId")
        private String offerId;

        @SerializedName("guests")
        private List<Guest> guests;

        @SerializedName("payment")
        private Payment payment;

        public Data(String offerId) {
            this.offerId = offerId;
            this.guests = new ArrayList<>();
            this.guests.add(new Guest());
            this.payment = new Payment();
        }
    }

    public static class Guest {
        @SerializedName("name")
        private Name name = new Name();

        @SerializedName("contact")
        private Contact contact = new Contact();

        public static class Name {
            @SerializedName("title")
            private String title = "MR";

            @SerializedName("firstName")
            private String firstName = "John";

            @SerializedName("lastName")
            private String lastName = "Doe";
        }

        public static class Contact {
            @SerializedName("phone")
            private String phone = "+11234567890";

            @SerializedName("email")
            private String email = "john.doe@example.com";
        }
    }

    public static class Payment {
        @SerializedName("method")
        private String method = "credit-card";

        @SerializedName("card")
        private Card card = new Card();

        public static class Card {

            @SerializedName("vendorCode")
            private String vendorCode = "VI";

            @SerializedName("cardNumber")
            private String cardNumber = "4111111111111111";

            @SerializedName("expiryDate")
            private String expiryDate = "2026-01";
        }
    }
}
