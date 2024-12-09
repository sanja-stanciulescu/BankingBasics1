package org.poo.accounts;

public class SavingsAccount extends ClassicAccount{
    private double interest;

    public SavingsAccount(String iban, String currency, String type, double interest) {
        super(iban, currency, type);
        this.interest = interest;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
}
