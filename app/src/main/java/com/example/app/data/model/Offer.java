package com.example.app.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Offer {
    @SerializedName("available")
    private boolean available;

    @NonNull
    @SerializedName("offers")
    private List<Offers> offers;

    @SerializedName("hotel")
    private SubHotel subHotel;


    public static class SubHotel {
        @NonNull
        @SerializedName("hotelId")
        private String id;

        @NonNull
        public String getId() {
            return id;
        }
    }

    public static class Offers {

        @NonNull
        @SerializedName("id")
        private String id;

        @SerializedName("room")
        private Room room;

        @SerializedName("price")
        private Price price;


        public static class Room {

            @SerializedName("typeEstimated")
            private TypeEstimated typeEstimated;


            public static class TypeEstimated {
                @SerializedName("beds")
                private int bed;

                public int getBed() {
                    return bed;
                }
            }


            public TypeEstimated getTypeEstimated() {
                return typeEstimated;
            }
        }


        public static class Price {
            @SerializedName("total")
            private String total;

            public String getTotal() {
                return total;
            }
        }


        @NonNull
        public String getId() {
            return id;
        }

        public Price getPrice() {
            return price;
        }

        public Room getRoom() {
            return room;
        }
    }


    public String getOfferId(int index) {
        return offers.get(index).getId();
    }

    public int getBed(int index) {
        return offers.get(index).getRoom().getTypeEstimated().getBed();
    }

    public boolean isAvailable() {
        return available;
    }

    public String getHotelId() {
        return subHotel.getId();
    }

    public String getPrice(int index) {
        return offers.get(index).getPrice().getTotal();
    }

    @NonNull
    public List<Offers> getOffers() {
        return offers;
    }
};