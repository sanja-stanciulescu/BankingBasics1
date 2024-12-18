package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.app.IBANRegistry;
import org.poo.fileio.CommandInput;
import org.poo.users.User;
import org.poo.utils.Utils;

public class AddAccountTransaction implements TransactionStrategy{
    private int timestamp;
    private String description;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User currentUser;
    @JsonIgnore
    private IBANRegistry registry;

    public AddAccountTransaction(final CommandInput command, final IBANRegistry registry, final User currentUser) {
        this.timestamp = command.getTimestamp();
        this.command = command;
        this.registry = registry;
        this.currentUser = currentUser;
    }

    @Override
    public void makeTransaction() {
        String iban = "";
        if (command.getAccountType().equals("classic")) {
            iban = Utils.generateIBAN();
            String currency = command.getCurrency();
            currentUser.getAccounts().add(new ClassicAccount(iban, currency, "classic"));
        } else if (command.getAccountType().equals("savings")) {
            iban = Utils.generateIBAN();
            String currency = command.getCurrency();
            double interest = command.getInterestRate();
            currentUser.getAccounts().add(new SavingsAccount(iban, currency, "savings", interest));
        }
        description = "New account created";
        registry.registerIBAN(iban, iban);
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

    public IBANRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(IBANRegistry registry) {
        this.registry = registry;
    }
}
