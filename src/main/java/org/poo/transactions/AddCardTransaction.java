package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.cards.OneTimeCard;
import org.poo.fileio.CommandInput;
import org.poo.users.User;
import org.poo.utils.Utils;

public class AddCardTransaction implements TransactionStrategy{
    private int timestamp;
    private String description;
    private String cardHolder;
    private String card;
    private String account;

    @JsonIgnore
    private CommandInput command;
    private User currentUser;

    public AddCardTransaction(CommandInput command, User currentUser) {
        this.command = command;
        this.currentUser = currentUser;
        timestamp = command.getTimestamp();
    }

    @Override
    public void makeTransaction() {
        int ret = searchIban(currentUser, command.getAccount());
        if (ret == 1) {
            account = command.getAccount();
            cardHolder = command.getEmail();
            description = "New card created";
            currentUser.getTransactions().add(this);
        } else {
            account = command.getAccount();
            cardHolder = command.getEmail();
            description = "Wrong user";
        }
    }

    private int searchIban(User user, String iban) {
        if (user == null) {
            return 0;
        }
        for (ClassicAccount account : user.getAccounts()) {
            if (account.getIban().equals(iban)) {
                String cardNumber = Utils.generateCardNumber();
                System.out.println("Card number: " + cardNumber);
                card = cardNumber;
                if (command.getCommand().equals("createCard")) {
                    account.getCards().add(new Card(cardNumber, "active"));
                } else if (command.getCommand().equals("createOneTimeCard")) {
                    account.getCards().add(new OneTimeCard(cardNumber));
                }
                if (account.getType().equals("classic")) {
                    account.getTransactions().add(this);
                }

                return 1;
            }
        }
        return 0;
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

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
