package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.poo.accounts.ClassicAccount;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMoneyTransaction implements TransactionStrategy{
    private String description;
    private int timestamp;
    private String senderIBAN;
    private String receiverIBAN;
    private String amount;
    private String transferType;

    @JsonIgnore
    private ClassicAccount giver;
    @JsonIgnore
    private User giverUser;
    @JsonIgnore
    private ClassicAccount receiver;
    @JsonIgnore
    private User receiverUser;
    @JsonIgnore
    private Bnr bank;
    @JsonIgnore
    private CommandInput command;

    public SendMoneyTransaction(CommandInput command, ClassicAccount giver, User giverUser, ClassicAccount receiver, User receiverUser, Bnr bank) {
        this.command = command;
        this.giver = giver;
        this.giverUser = giverUser;
        this.receiver = receiver;
        this.receiverUser = receiverUser;
        this.bank = bank;
        this.timestamp = command.getTimestamp();
        this.description = command.getDescription();
    }

    public SendMoneyTransaction(String description, int timestamp, String senderIBAN, String receiverIBAN, String amount, String transferType) {
        this.description = description;
        this.timestamp = timestamp;
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount;
        this.transferType = transferType;
    }

    public void makeTransaction() {
        if (giver == null || receiver == null) {
            System.out.println("Cannot make transaction");
            return;
        }

        if (giver.getBalance() - command.getAmount() <= 0) {
            description = "Insufficient funds";
        } else {
            senderIBAN = giver.getIban();
            receiverIBAN = receiver.getIban();
            amount = command.getAmount() + " " + giver.getCurrency();
            transferType = "sent";
            double amount;
            if (!giver.getCurrency().equals(receiver.getCurrency())) {
                double exchangeRate = bank.getExchangeRate(giver.getCurrency(), receiver.getCurrency());
                amount = command.getAmount() * exchangeRate;
            } else {
                amount = command.getAmount();
            }
            giver.setBalance(giver.getBalance() - command.getAmount());
            receiver.setBalance(receiver.getBalance() + amount);
            TransactionStrategy trans = new SendMoneyTransaction(description, timestamp, senderIBAN, receiverIBAN, amount + " " + receiver.getCurrency(), "received");
            receiverUser.getTransactions().add(trans);
            receiver.getTransactions().add(trans);
        }
        if (giverUser.getEmail().equals(receiverUser.getEmail())) {
            giverUser.getTransactions().add(giverUser.getTransactions().size() - 1, this);
        } else {
            giverUser.getTransactions().add(this);
        }
        if (giver.getType().equals("classic")) {
            giver.getTransactions().add(this);
        }
        System.out.println("Could make transaction");
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

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public void setSenderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    public String getReceiverIBAN() {
        return receiverIBAN;
    }

    public void setReceiverIBAN(String receiverIBAN) {
        this.receiverIBAN = receiverIBAN;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
}
