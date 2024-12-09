package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;
import org.poo.utils.Utils;

public class AddAccountTransaction implements TransactionStrategy{
    private int timestamp;
    private String description;

    @JsonIgnore
    private CommandInput command;
    private User currentUser;

    public AddAccountTransaction(final CommandInput command, final User currentUser) {
        this.timestamp = command.getTimestamp();
        this.description = command.getDescription();
        this.command = command;
        this.currentUser = currentUser;
    }

    @Override
    public void makeTransaction() {
        if (command.getAccountType().equals("classic")) {
            String iban = Utils.generateIBAN();
            String currency = command.getCurrency();
            currentUser.getAccounts().add(new ClassicAccount(iban, currency, "classic"));
        } else if (command.getAccountType().equals("savings")) {
            String iban = Utils.generateIBAN();
            String currency = command.getCurrency();
            double interest = command.getInterestRate();
            currentUser.getAccounts().add(new SavingsAccount(iban, currency, "savings", interest));
        }
        currentUser.getTransactions().add(this);
    }


    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CommandInput getCommand() {
        return command;
    }

    public void setCommand(CommandInput command) {
        this.command = command;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
