package org.poo.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.poo.cards.Card;

import java.util.ArrayList;

public class ClassicAccount {
    private double balance;
    private String currency;
    private String type;
    private ArrayList<Card> cards;

    @JsonProperty("IBAN")
    private String iban;

    @JsonIgnore
    private double minBalance;

    public ClassicAccount(String iban, String currency, String type) {
        this.iban = iban;
        this.currency = currency;
        this.type = type;
        balance = 0;
        cards = new ArrayList<>();
    }

    public void changeInterest(double interest) {}

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double currentBalance) {
        this.balance = currentBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(double minBalance) {
        this.minBalance = minBalance;
    }
}
