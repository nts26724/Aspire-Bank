package com.example.app.data.model;

import androidx.annotation.NonNull;

public class Customer {
    @NonNull
    private String username;
    private String fullName;
    private String birthDay;
    private String address;
    private String phoneNumber;
    private String email;
    private String gender;

    public Customer() {}

    public Customer(@NonNull String username, String fullName, String birthDay, String address, String phoneNumber, String email, String gender) {
        this.username = username;
        this.fullName = fullName;
        this.birthDay = birthDay;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
