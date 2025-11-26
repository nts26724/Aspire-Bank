package com.example.app.data.model;

import androidx.annotation.NonNull;

public class Account {
    @NonNull
    private String username;
    private String password;
    private String role;
    private double balance;
    private double mortgage;
    private double saving;


    public Account() {}

    public Account(@NonNull String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.balance = 0;
        this.mortgage = 0;
        this.saving = 0;
    }

    public double getBalance() {
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

    public double getMortgage() {
        return mortgage;
    }

    public double getSaving() {
        return saving;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setMortgage(double mortgage) {
        this.mortgage = mortgage;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSaving(double saving) {
        this.saving = saving;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }
}
