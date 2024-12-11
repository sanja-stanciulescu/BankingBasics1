package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class PayOnlineTransaction implements TransactionStrategy{
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    private User currentUser;
    private ArrayNode output;
    private Bnr bank;

    public PayOnlineTransaction(final CommandInput command, final ArrayNode output,  final Bnr bank, final User currentUser) {
        this.command = command;
        this.output = output;
        this.bank = bank;
        this.currentUser = currentUser;
        this.description = command.getDescription();
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
        ClassicAccount account = pickCard(command.getCardNumber());
            if (account == null) {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode cardNode = mapper.createObjectNode();
                cardNode.put("command", command.getCommand());
                cardNode.put("timestamp", timestamp);

                ObjectNode errorNode = mapper.createObjectNode();
                errorNode.put("description", "Card not found");
                errorNode.put("timestamp", timestamp);
                cardNode.set("output", errorNode);
                output.add(cardNode);
            } else {
                String currency = account.getCurrency();
                double amount;
                if (!currency.equals(command.getCurrency())) {
                    double exchangeRate = bank.getExchangeRate(command.getCurrency(), currency);
                    amount = command.getAmount() * exchangeRate;
                } else {
                    amount = command.getAmount();
                }
                if (account.getBalance() - amount <= account.getMinBalance()) {
                    System.out.println("Insufficient funds");
                } else {
                    account.setBalance(account.getBalance() - amount);
                }
            }
    }

    private ClassicAccount pickCard(final String cardNumber) {
        for (ClassicAccount account : currentUser.getAccounts()) {
            for (int i = 0; i < account.getCards().size(); i++) {
                if (account.getCards().get(i).getCardNumber().equals(cardNumber)) {
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
}
