package com.example.app.data.model;

public class User {
    private String username;
    private String fullName;
    private String cccd;
    private String email;
    private String password;

    public User(String username, String fullName, String cccd, String email) {
        this.username = username;
        this.fullName = fullName;
        this.cccd = cccd;
        this.email = email;
        this.password = "123456";
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
}