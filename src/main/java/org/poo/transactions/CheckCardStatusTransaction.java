package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckCardStatusTransaction implements TransactionStrategy {
    private String description;
    private Integer timestamp;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User user;
    @JsonIgnore
    private ClassicAccount currentAccount;
    @JsonIgnore
    private Card card;
    @JsonIgnore
    private ArrayNode output;

    public CheckCardStatusTransaction(final CommandInput command, final ArrayNode output, final User user, final ClassicAccount currentAccount, final Card card) {
        this.command = command;
        this.output = output;
        this.user = user;
        this.currentAccount = currentAccount;
        this.card = card;
    }

    public void makeTransaction() {
        if (currentAccount == null || card == null) {
            printError(command, command.getTimestamp(), output);
        } else {
         if (currentAccount.getBalance() <= currentAccount.getMinBalance()) {
             description = "You have reached the minimum amount of funds, the card will be frozen";
             timestamp = command.getTimestamp();
             card.setStatus("frozen");
             user.getTransactions().add(this);
         } else if (currentAccount.getBalance() - currentAccount.getMinBalance() <= 30) {
             description = "The card is in a warning stage";
             timestamp = command.getTimestamp();
             card.setStatus("warning");
             user.getTransactions().add(this);
         }
        }
    }

    static void printError(CommandInput command, int timestamp, ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cardNode = mapper.createObjectNode();
        cardNode.put("command", command.getCommand());
        cardNode.put("timestamp", command.getTimestamp());

        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("description", "Card not found");
        errorNode.put("timestamp", command.getTimestamp());
        cardNode.set("output", errorNode);
        output.add(cardNode);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getTimestamp() {
        if (timestamp != null) {
            return timestamp;
        }
        return -1;
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

    public ClassicAccount getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(ClassicAccount currentAccount) {
        this.currentAccount = currentAccount;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public void setOutput(ArrayNode output) {
        this.output = output;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
