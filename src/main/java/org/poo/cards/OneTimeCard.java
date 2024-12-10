package org.poo.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OneTimeCard extends Card{
    @JsonIgnore
    private int hasBeenUsed;

    public OneTimeCard(String cardNumber) {
        super(cardNumber, "active");
        hasBeenUsed = 0;
    }


    public int getHasBeenUsed() {
        return hasBeenUsed;
    }

    public void setHasBeenUsed(int hasBeenUsed) {
        this.hasBeenUsed = hasBeenUsed;
    }
}
