package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class ChangeInterestTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User user;
    @JsonIgnore
    private ClassicAccount account;

    public ChangeInterestTransaction(final CommandInput command, final User user, final ClassicAccount account) {
        this.command = command;
        this.user = user;
        this.account = account;
        this.timestamp = command.getTimestamp();
        description = "Interest rate of the account changed to " + command.getInterestRate();
    }

    public void makeTransaction() {
        if (user == null || account == null) {
            System.out.println("Could not change interest rate");
        }
        account.changeInterest(command.getInterestRate());
        account.getTransactions().add(this);
        user.getTransactions().add(this);
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
}
