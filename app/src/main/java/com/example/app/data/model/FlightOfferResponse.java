package com.example.app.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FlightOfferResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }


    public static class Data {

        @SerializedName("offers")
        private List<Offer> offers;

        public List<Offer> getOffers() {
            return offers;
        }
    }



    public static class Offer {

        @SerializedName("id")
        private String id;

        @SerializedName("owner")
        private Owner owner;

        @SerializedName("total_amount")
        private String totalAmount;

        @SerializedName("total_currency")
        private String totalCurrency;

        @SerializedName("slices")
        private List<Slice> slices;

        public String getId() {
            return id;
        }

        public Owner getOwner() {
            return owner;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public String getTotalCurrency() {
            return totalCurrency;
        }

        public List<Slice> getSlices() {
            return slices;
        }

        public static class Owner {

            @SerializedName("name")
            private String name;

            @SerializedName("iata_code")
            private String iataCode;

            public String getName() {
                return name;
            }

            public String getIataCode() {
                return iataCode;
            }
        }

        public static class Slice {

            @SerializedName("duration")
            private String duration;

            @SerializedName("origin")
            private Origin origin;

            @SerializedName("destination")
            private Destination destination;

            @SerializedName("segments")
            private List<Segment> segments;

            public String getDuration() {
                return duration;
            }

            public Origin getOrigin() {
                return origin;
            }

            public Destination getDestination() {
                return destination;
            }

            public List<Segment> getSegments() {
                return segments;
            }

            public static class Origin {
                @SerializedName("iata_code")
                private String iataCode;

                public String getIataCode() {
                    return iataCode;
                }
            }

            public static class Destination {
                @SerializedName("iata_code")
                private String iataCode;

                public String getIataCode() {
                    return iataCode;
                }
            }

            public static class Segment {

                @SerializedName("departing_at")
                private String departingAt;

                @SerializedName("arriving_at")
                private String arrivingAt;

                public String getDepartingAt() {
                    return departingAt;
                }

                public String getArrivingAt() {
                    return arrivingAt;
                }
            }
        }
    }

}
