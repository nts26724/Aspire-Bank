package com.example.app.data.model;

import androidx.annotation.NonNull;

public class Receipt {
    @NonNull
    private String receiptID;
    private String username;
    private long amount;
    private String type;
    private long timestamp;

    public Receipt() {}

    public Receipt(@NonNull String receiptID, String username, long amount, String type, long timestamp) {
        this.receiptID = receiptID;
        this.username = username;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    @NonNull
    public String getReceiptID() {
        return receiptID;
    }

    public String getUsername() {
        return username;
    }

    public long getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setReceiptID(@NonNull String receiptID) {
        this.receiptID = receiptID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
