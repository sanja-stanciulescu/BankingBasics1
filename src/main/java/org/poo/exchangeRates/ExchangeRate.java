package org.poo.exchangeRates;

import org.poo.fileio.ExchangeInput;

public class ExchangeRate {
    private String from;
    private String to;
    private double rate;
    private int timestamp;

    public ExchangeRate(final String from, final String to, final double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    public ExchangeRate(final ExchangeInput other) {
        this.from = other.getFrom();
        this.to = other.getTo();
        this.rate = other.getRate();
        this.timestamp = other.getTimestamp();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
