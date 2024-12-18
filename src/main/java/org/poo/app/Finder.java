package org.poo.app;

import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.users.User;

public class Finder {
    private User user;
    private ClassicAccount account;
    private Card card;

    public Finder() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ClassicAccount getAccount() {
        return account;
    }

    public void setAccount(ClassicAccount account) {
        this.account = account;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
