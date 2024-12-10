package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class DeleteCardTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    private ClassicAccount currentAccount;
    private User currentUser;

    public DeleteCardTransaction(final CommandInput command, final ClassicAccount currentAccount, final User currentUser) {
        this.command = command;
        this.currentAccount = currentAccount;
        this.currentUser = currentUser;
        this.description = command.getDescription();
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
        if (currentAccount != null) {
            int idx = pickCard(command.getCardNumber());
            if (idx == -1) {
                System.out.println("Card not found");
            } else {
                currentAccount.getCards().remove(idx);
                if (currentUser != null) {
                    currentUser.getTransactions().add(this);
                }
            }
        }
    }

    private int pickCard(final String cardNumber) {
        for (int i = 0; i < currentAccount.getCards().size(); i++) {
            if (currentAccount.getCards().get(i).getCardNumber().equals(cardNumber)) {
                return i;
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

    public ClassicAccount getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(ClassicAccount currentAccount) {
        this.currentAccount = currentAccount;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
