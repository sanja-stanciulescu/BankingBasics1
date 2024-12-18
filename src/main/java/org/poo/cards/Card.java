package org.poo.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Card {
    protected String cardNumber;
    protected String status;

    @JsonIgnore
    private int checkedStatus;

    public Card(String cardNumber, String status) {
        this.cardNumber = cardNumber;
        this.status = status;
        checkedStatus = 0;
    }

    public void useCard() {}

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(int checkedStatus) {
        this.checkedStatus = checkedStatus;
    }
}
