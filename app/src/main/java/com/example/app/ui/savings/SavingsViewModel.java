package com.example.app.ui.savings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SavingsViewModel extends ViewModel {

    private final MutableLiveData<Double> interestAmount = new MutableLiveData<>();
    private final MutableLiveData<Double> totalAmount = new MutableLiveData<>();

    public void calculateInterest(double amount, String term, double rateYear) {
        int months = 1;
        try {
            months = Integer.parseInt(term.replace(" tháng", "").trim());
        } catch (Exception e) {
            months = 1;
        }

        double interest = (amount * rateYear / 100) / 12 * months;
        double total = amount + interest;

        interestAmount.setValue(interest);
        totalAmount.setValue(total);
    }

    public LiveData<Double> getInterestAmount() {
        return interestAmount;
    }

    public LiveData<Double> getTotalAmount() {
        return totalAmount;
    }
}