package org.poo.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.poo.cards.Card;
import org.poo.commerciants.CommerciantCatgeory;
import org.poo.transactions.TransactionStrategy;

import java.util.ArrayList;

public class ClassicAccount {
    protected double balance;
    protected String currency;
    protected String type;
    protected ArrayList<Card> cards;

    @JsonIgnore
    private ArrayList<TransactionStrategy> transactions;
    @JsonIgnore
    private ArrayList<CommerciantCatgeory> commerciants;

    @JsonProperty("IBAN")
    protected String iban;

    @JsonIgnore
    protected double minBalance;

    public ClassicAccount(String iban, String currency, String type) {
        this.iban = iban;
        this.currency = currency;
        this.type = type;
        balance = 0;
        cards = new ArrayList<>();
        transactions = new ArrayList<>();
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

    public ArrayList<TransactionStrategy> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<TransactionStrategy> transactions) {
        this.transactions = transactions;
    }
}
