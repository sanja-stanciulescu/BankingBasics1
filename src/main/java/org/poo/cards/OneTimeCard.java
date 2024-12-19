package org.poo.cards;

import org.poo.transactions.AddCardTransaction;
import org.poo.transactions.DeleteCardTransaction;
import org.poo.transactions.TransactionStrategy;
import org.poo.users.User;
import org.poo.utils.Utils;

public class OneTimeCard extends Card{

    public OneTimeCard(String cardNumber) {
        super(cardNumber, "active");
    }

    @Override
    public int useCard(String account, User user, String cardHolder, int timestamp) {
        TransactionStrategy trans = new DeleteCardTransaction("The card has been destroyed", timestamp, account, cardNumber, cardHolder);
        user.getTransactions().add(trans);
        cardNumber = Utils.generateCardNumber();
        trans = new AddCardTransaction(account, cardNumber, cardHolder, "New card created", timestamp);
        user.getTransactions().add(trans);
        status = "active";

        return 1;
    }
}
