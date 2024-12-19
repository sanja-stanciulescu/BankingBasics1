package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class DeleteCardTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;
    private String account;
    private String card;
    private String cardHolder;


    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private ClassicAccount currentAccount;
    @JsonIgnore
    private User currentUser;

    public DeleteCardTransaction(final CommandInput command, final ClassicAccount currentAccount, final User currentUser) {
        this.command = command;
        this.currentAccount = currentAccount;
        this.currentUser = currentUser;
        this.timestamp = command.getTimestamp();
    }

    public DeleteCardTransaction(final String description, final int timestamp, final String account, final String card, final String cardHolder) {
        this.description = description;
        this.timestamp = timestamp;
        this.account = account;
        this.card = card;
        this.cardHolder = cardHolder;
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
                    description = "The card has been destroyed";
                    account = currentAccount.getIban();
                    card = command.getCardNumber();
                    cardHolder = currentUser.getEmail();
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
}
