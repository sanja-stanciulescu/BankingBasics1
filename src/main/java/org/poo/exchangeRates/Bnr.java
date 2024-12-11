package org.poo.exchangeRates;

import org.poo.fileio.ObjectInput;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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
        Queue<ExchangeRateNode> queue = new LinkedList<>();
        ArrayList<String> visited = new ArrayList<>();

        queue.add(new ExchangeRateNode(from, 1.0));
        visited.add(from);

        while (!queue.isEmpty()) {
            ExchangeRateNode currentNode = queue.poll();
            String currentCurrency = currentNode.getCurrency();
            double currentRate = currentNode.getRate();

            if (currentCurrency.equals(to)) {
                return currentRate;
            }

            for (ExchangeRate rate : exchangeRates) {
                if (rate.getFrom().equals(currentCurrency) && !visited.contains(rate.getTo())) {
                    visited.add(rate.getTo());
                    queue.add(new ExchangeRateNode(rate.getTo(), currentRate * rate.getRate()));
                }
            }
        }

        return -1;
    }

    public ArrayList<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(ArrayList<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

}
