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
        description = command.getDescription();
    }

    @Override
    public void makeTransaction() {
        int ret = searchIban(currentUser, command.getAccount());
        if (ret == 1) {
            account = command.getAccount();
            cardHolder = command.getEmail();
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
                this.card = cardNumber;
                if (command.getCommand().equals("createCard")) {
                    account.getCards().add(new Card(cardNumber, "active"));
                } else if (command.getCommand().equals("createOneTimeCard")) {
                    account.getCards().add(new OneTimeCard(cardNumber));
                }

                return 1;
            }
        }
        return 0;
    }
}
