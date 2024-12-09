package org.poo.cards;

public class OneTimeCard extends Card{
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
