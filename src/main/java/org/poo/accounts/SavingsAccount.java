package org.poo.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SavingsAccount extends ClassicAccount{
    @JsonIgnore
    private double interest;

    public SavingsAccount(String iban, String currency, String type, double interest) {
        super(iban, currency, type);
        this.interest = interest;
    }

    @Override
    public void changeInterest(double interest) {
        this.interest = interest;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
}
