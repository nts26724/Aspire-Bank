package com.example.app.data.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class Transaction {
    @NonNull
    private String transactionID;
    private String usernameTransfer;
    private String usernameReceive;
    private long amount;
    private String content;
    private long time;
    private boolean transfer;

    public Transaction() {}

    public Transaction(@NonNull String transactionID, String usernameTransfer,
                       String usernameReceive, long amount, String content,
                       long time, boolean transfer) {

        this.transactionID = transactionID;
        this.usernameTransfer = usernameTransfer;
        this.usernameReceive = usernameReceive;
        this.amount = amount;
        this.content = content;
        this.time = time;
        this.transfer = transfer;
    }

    @NonNull
    public String getTransactionID() {
        return transactionID;
    }

    public String getUsernameReceive() {
        return usernameReceive;
    }

    public String getUsernameTransfer() {
        return usernameTransfer;
    }

    public long getAmount() {
        return amount;
    }

    public String getContent() {
        return content;
    }

    public void setTransactionID(@NonNull String transactionID) {
        this.transactionID = transactionID;
    }

    public void setUsernameReceive(String usernameReceive) {
        this.usernameReceive = usernameReceive;
    }

    public void setUsernameTransfer(String usernameTransfer) {
        this.usernameTransfer = usernameTransfer;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }
    public boolean isTransfer() {
        return transfer;
    }
}
