package org.poo.commerciants;

import org.poo.transactions.PayOnlineTransaction;

import java.util.ArrayList;

public class Commerciant {
    private ArrayList<PayOnlineTransaction> payments;

   public Commerciant() {
       payments = new ArrayList<>();
   }

    public ArrayList<PayOnlineTransaction> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<PayOnlineTransaction> payments) {
        this.payments = payments;
    }
}
