package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.exchangeRates.Bnr;
import org.poo.exchangeRates.ExchangeRate;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayOnlineTransaction implements TransactionStrategy{
    private String description;
    private int timestamp;
    private Double amount;
    private String commerciant;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User currentUser;
    @JsonIgnore
    private ArrayNode output;
    @JsonIgnore
    private Bnr bank;

    public PayOnlineTransaction(final CommandInput command, final ArrayNode output,  final Bnr bank, final User currentUser) {
        this.command = command;
        this.output = output;
        this.bank = bank;
        this.currentUser = currentUser;
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
        ClassicAccount account = pickCard(command.getCardNumber());
            if (account == null) {
                CheckCardStatusTransaction.printError(command, timestamp, output);
            } else {
                String currency = account.getCurrency();
                double amount;
                if (description != null) {
                    currentUser.getTransactions().add(this);
                    return;
                }
                if (!currency.equals(command.getCurrency())) {
                    double exchangeRate = bank.getExchangeRate(command.getCurrency(), currency);
                    bank.getExchangeRates().add(new ExchangeRate(command.getCurrency(), currency, exchangeRate));
                    amount = command.getAmount() * exchangeRate;
                } else {
                    amount = command.getAmount();
                }
                if (account.getBalance() - amount <= account.getMinBalance()) {
                    description = "Insufficient funds";
                } else {
                    account.setBalance(account.getBalance() - amount);

                    this.amount = amount;
                    commerciant = command.getCommerciant();
                    description = "Card payment";
                }
                currentUser.getTransactions().add(this);
            }
    }

    private ClassicAccount pickCard(final String cardNumber) {
        for (ClassicAccount account : currentUser.getAccounts()) {
            for (int i = 0; i < account.getCards().size(); i++) {
                if (account.getCards().get(i).getCardNumber().equals(cardNumber)) {
                    if (account.getCards().get(i).getStatus().equals("frozen")) {
                        description = "The card is frozen";
                    } else {
                        account.getCards().get(i).useCard();
                    }
                    if (account.getType().equals("classic")) {
                        account.getTransactions().add(this);
                    }
                    return account;
                }
            }
        }

        return null;
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

    public CommandInput getCommand() {
        return command;
    }

    public void setCommand(CommandInput command) {
        this.command = command;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public void setOutput(ArrayNode output) {
        this.output = output;
    }

    public Bnr getBank() {
        return bank;
    }

    public void setBank(Bnr bank) {
        this.bank = bank;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public void setCommerciant(String commerciant) {
        this.commerciant = commerciant;
    }
}
