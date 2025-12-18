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
    private String cardNumber;

    private String phoneNumber;
    private String email;
    private String address;
    private String dob;

    public Account() {}

    public Account(@NonNull String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.balance = 0;
        this.mortgage = 0;
        this.saving = 0;

        this.phoneNumber = "Chưa cập nhật";
        this.email = "Chưa cập nhật";
        this.address = "Chưa cập nhật";
        this.dob = "01/01/2000";
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void withdraw(long amount) {
        this.balance -= amount;
    }

    public void deposit(long amount) {
        this.balance += amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}