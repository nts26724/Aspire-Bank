package com.example.app.data.model;

public class InterestRate {
    private int termMonths;
    private double savingsRate;
    private double mortgageRate;

    public InterestRate(int termMonths, double savingsRate, double mortgageRate) {
        this.termMonths = termMonths;
        this.savingsRate = savingsRate;
        this.mortgageRate = mortgageRate;
    }

    public int getTermMonths() { return termMonths; }
    public double getSavingsRate() { return savingsRate; }
    public double getMortgageRate() { return mortgageRate; }
}