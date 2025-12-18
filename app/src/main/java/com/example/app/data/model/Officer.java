package com.example.app.data.model;

import androidx.annotation.NonNull;

public class Officer {
    @NonNull
    private String username;
    private String address;
    private String birthDay;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String gender;

    public Officer() {}

    public Officer(String username, String address,
                   String birthDay, String email,
                   String fullName, String phoneNumber,
                   String gender) {
        this.username = username;
        this.address = address;
        this.birthDay = birthDay;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public String getAddress() {
        return address;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
