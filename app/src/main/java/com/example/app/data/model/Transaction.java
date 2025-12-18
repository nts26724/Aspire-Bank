package com.example.app.data.model;

public class Transaction {
    private long transactionID;
    private long amount;
    private String content;
    private long time;
    private boolean transfer;
    private String usernameTransfer;
    private String usernameReceive;

    public Transaction() {
    }

    public Transaction(long transactionID, long amount, String content, long time, boolean transfer, String usernameTransfer, String usernameReceive) {
        this.transactionID = transactionID;
        this.amount = amount;
        this.content = content;
        this.time = time;
        this.transfer = transfer;
        this.usernameTransfer = usernameTransfer;
        this.usernameReceive = usernameReceive;
    }

    public long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(long transactionID) {
        this.transactionID = transactionID;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
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

    public boolean isTransfer() {
        return transfer;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    public String getUsernameTransfer() {
        return usernameTransfer;
    }

    public void setUsernameTransfer(String usernameTransfer) {
        this.usernameTransfer = usernameTransfer;
    }

    public String getUsernameReceive() {
        return usernameReceive;
    }

    public void setUsernameReceive(String usernameReceive) {
        this.usernameReceive = usernameReceive;
    }
}