package org.poo.exchangeRates;

import org.poo.fileio.ObjectInput;

import java.util.ArrayList;

public class Bnr {
    private ArrayList<ExchangeRate> exchangeRates;

    public Bnr() {
        exchangeRates = new ArrayList<>();
    }

    public void setUp(final ObjectInput inputData) {
        int length;

        if (inputData.getExchangeRates() != null) {
            length = inputData.getExchangeRates().length;
        } else {
            length = 0;
        }
        for (int i = 0; i < length; i++) {
            exchangeRates.add(new ExchangeRate(inputData.getExchangeRates()[i]));
        }

        coverAllExchangeRates();
    }

    private void coverAllExchangeRates() {
        int size = exchangeRates.size();
        for (int i = 0; i < size; i++) {
            ExchangeRate excR = exchangeRates.get(i);
            exchangeRates.add(new ExchangeRate(excR.getTo(), excR.getFrom(), 1 / excR.getRate()));
        }
    }

    public double getExchangeRate(String from, String to) {


    }

    public double getExchangeRate(String from, String to) {
        double rate = 1;
        for (ExchangeRate exchangeRate : exchangeRates) {
            if (exchangeRate.getFrom().equals(from) && exchangeRate.getTo().equals(to)) {
                return exchangeRate.getRate();
            }

            if (exchangeRate.getFrom().equals(from) && !exchangeRate.getTo().equals(to)) {
                rate = rate * getExchangeRate(exchangeRate.getTo(), to);
            }
        }

        return rate;
    }

    public ArrayList<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(ArrayList<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

}
