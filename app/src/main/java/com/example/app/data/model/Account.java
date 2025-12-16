package com.example.app.data.model;

import androidx.annotation.NonNull;

public class Account {
    @NonNull
    private String username;
    private String password;
    private String role;
    private String accountNumber;
    private String cardNumber;
    private String expiryDate;
    private long balance;
    private long mortgage;
    private long saving;

    private String phoneNumber;
    private String email;
    private String address;
    private String dob;

    public Account() {
        this.role = "customer";
        this.balance = 0;
        this.mortgage = 0;
        this.saving = 0;
    }

    public Account(@NonNull String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = (role != null) ? role : "customer";
        this.balance = 0;
        this.mortgage = 0;
        this.saving = 0;
        this.accountNumber = "123" + username;
        this.cardNumber = "9704 " + username;
        this.expiryDate = "12/30";

        this.phoneNumber = "Chưa cập nhật";
        this.email = "Chưa cập nhật";
        this.address = "Chưa cập nhật";
        this.dob = "01/01/2000";
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getMortgage() {
        return mortgage;
    }

    public void setMortgage(long mortgage) {
        this.mortgage = mortgage;
    }

    public long getSaving() {
        return saving;
    }

    public void setSaving(long saving) {
        this.saving = saving;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
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
}