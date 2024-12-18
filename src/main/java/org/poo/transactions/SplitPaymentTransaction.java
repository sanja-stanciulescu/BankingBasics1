package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.app.Finder;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class SplitPaymentTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;
    private double amount;
    private String currency;
    private ArrayList<String> involvedAccounts;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private ArrayList<Finder> finders;
    @JsonIgnore
    private Bnr bank;

    public SplitPaymentTransaction(final CommandInput command, final ArrayList<Finder> finders, final Bnr bank) {
        this.command = command;
        this.finders = finders;
        this.bank = bank;
        this.timestamp = command.getTimestamp();
        this.involvedAccounts = new ArrayList<>();
    }

    public void makeTransaction() {
        if (!checkAccounts(finders)) {
            System.out.println("Nu se poate");
        } else {
            amount = command.getAmount() / finders.size();
            currency = command.getCurrency();
            description = "Split payment of " + String.format("%.2f", command.getAmount()) + " " + currency;
            involvedAccounts.addAll(command.getAccounts());
            for (Finder finder : finders) {
               double actualAmount = convertAmount(finders, finder);
               finder.getAccount().setBalance(finder.getAccount().getBalance() - actualAmount);
               finder.getUser().getTransactions().add(this);
            }
        }
    }

    private boolean checkAccounts(ArrayList<Finder> finders) {
        for (Finder finder : finders) {
            if (finder.getUser() == null || finder.getAccount() == null) {
                return false;
            }
            double amount = convertAmount(finders, finder);
            if (Double.compare(finder.getAccount().getBalance(), amount) < 0) {
                return false;
            }
        }

        return true;
    }

    private double convertAmount(ArrayList<Finder> finders, Finder finder) {
        double amount;
        if (!command.getCurrency().equals(finder.getAccount().getCurrency())) {
            double exchangeRate = bank.getExchangeRate(command.getCurrency(), finder.getAccount().getCurrency());
            amount = command.getAmount() / finders.size() * exchangeRate;
        } else {
            amount = command.getAmount() / finders.size();
        }
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ArrayList<String> getInvolvedAccounts() {
        return involvedAccounts;
    }

    public void setInvolvedAccounts(ArrayList<String> involvedAccounts) {
        this.involvedAccounts = involvedAccounts;
    }
}
