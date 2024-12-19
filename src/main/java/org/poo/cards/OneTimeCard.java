package org.poo.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.utils.Utils;

public class OneTimeCard extends Card{

    public OneTimeCard(String cardNumber) {
        super(cardNumber, "active");
    }

    @Override
    public void useCard() {
        cardNumber = Utils.generateCardNumber();
        System.out.println(cardNumber);
        status = "active";
    }
}
