package org.poo.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.UserInput;
import org.poo.transactions.TransactionStrategy;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<ClassicAccount> accounts;

    @JsonIgnore
    private ArrayList<TransactionStrategy> transactions;

    public User(UserInput other) {
        this.firstName = other.getFirstName();
        this.lastName = other.getLastName();
        this.email = other.getEmail();
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<ClassicAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<ClassicAccount> accounts) {
        this.accounts = accounts;
    }

    public ArrayList<TransactionStrategy> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<TransactionStrategy> transactions) {
        this.transactions = transactions;
    }
}
