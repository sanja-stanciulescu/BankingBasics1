package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class PayOnlineTransaction implements TransactionStrategy{
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    private User currentUser;

    public PayOnlineTransaction(final CommandInput command, final User currentUser) {
        this.command = command;
        this.currentUser = currentUser;
        this.description = command.getDescription();
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {

    }

    private int pickCard(final String cardNumber) {
        for (ClassicAccount account : currentUser.getAccounts()) {
            for (int i = 0; i < account.getCards().size(); i++) {
                if (account.getCards().get(i).getCardNumber().equals(cardNumber)) {
                    return i;
                }
            }
        }

        return -1;
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
