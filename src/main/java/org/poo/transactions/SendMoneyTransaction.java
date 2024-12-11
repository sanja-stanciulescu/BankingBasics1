package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;

public class SendMoneyTransaction implements TransactionStrategy{
    private String description;
    private int timestamp;

    @JsonIgnore
    private ClassicAccount giver;
    private ClassicAccount receiver;
    private Bnr bank;
    private CommandInput command;

    public SendMoneyTransaction(CommandInput command, ClassicAccount giver, ClassicAccount receiver, Bnr bank) {
        this.command = command;
        this.giver = giver;
        this.receiver = receiver;
        this.bank = bank;
        this.timestamp = command.getTimestamp();
        this.description = command.getDescription();
    }

    public void makeTransaction() {
        if (giver == null || receiver == null) {
            System.out.println("Cannot make transaction");
            return;
        }
        if (giver.getBalance() - command.getAmount() <= 0) {
            System.out.println("Insufficient funds to make a bank transfer");
        } else {
            double amount;
            if (!giver.getCurrency().equals(receiver.getCurrency())) {
                double exchangeRate = bank.getExchangeRate(giver.getCurrency(), receiver.getCurrency());
                amount = command.getAmount() * exchangeRate;
            } else {
                amount = command.getAmount();
            }
            giver.setBalance(giver.getBalance() - command.getAmount());
            receiver.setBalance(receiver.getBalance() + amount);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public ClassicAccount getGiver() {
        return giver;
    }

    public void setGiver(ClassicAccount giver) {
        this.giver = giver;
    }

    public ClassicAccount getReceiver() {
        return receiver;
    }

    public void setReceiver(ClassicAccount receiver) {
        this.receiver = receiver;
    }

    public CommandInput getCommand() {
        return command;
    }

    public void setCommand(CommandInput command) {
        this.command = command;
    }

    public Bnr getBank() {
        return bank;
    }

    public void setBank(Bnr bank) {
        this.bank = bank;
    }
}
