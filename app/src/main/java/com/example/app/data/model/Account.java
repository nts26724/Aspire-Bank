package com.example.app.data.model;

import androidx.annotation.NonNull;

public class Account {
    @NonNull
    private String username;
    private String password;
    private String role;
    private long balance;
    private long mortgage;
    private long saving;


    public Account() {}

    public Account(@NonNull String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.balance = 0;
        this.mortgage = 0;
        this.saving = 0;
    }

    public long getBalance() {
        return balance;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public long getMortgage() {
        return mortgage;
    }

    public long getSaving() {
        return saving;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void setMortgage(long mortgage) {
        this.mortgage = mortgage;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSaving(long saving) {
        this.saving = saving;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void widthraw(long amount) {
        this.balance -= amount;
    }

    public void deposit(long amount) {
        this.balance += amount;
    }
}
