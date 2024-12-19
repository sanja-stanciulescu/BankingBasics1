package org.poo.commerciants;

public class CommerciantCatgeory {
    private String commerciant;
    private double total;

    public CommerciantCatgeory(String commerciant, double total) {
        this.commerciant = commerciant;
        this.total = total;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public void setCommerciant(String commerciant) {
        this.commerciant = commerciant;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
