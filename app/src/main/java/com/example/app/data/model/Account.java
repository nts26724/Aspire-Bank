package com.example.app.data.model;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class Account {
    @NonNull
    private String username;
    private String password;
    private String role;
    private String cardNumber;
    private String expiryDate;
    private long balance;
    private long mortgage;
    private long saving;

    public Account() {
    }

    public Account(@NonNull String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = (role != null) ? role : "customer";
        this.balance = 0;
        this.mortgage = 0;
        this.saving = 0;
        this.cardNumber = generateRandomNumber(12);
        this.expiryDate = calculateExpiryDate(5);
    }

    private String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String calculateExpiryDate(int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, years);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return sdf.format(calendar.getTime());
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

    public void withdraw(long amount) {
        this.balance -= amount;
    }

    public void deposit(long amount) {
        this.balance += amount;
    }

}