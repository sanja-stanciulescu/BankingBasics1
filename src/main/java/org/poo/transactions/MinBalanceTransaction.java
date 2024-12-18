package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class MinBalanceTransaction implements TransactionStrategy{
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    private User currentUser;
    private ClassicAccount account;
    private ArrayNode output;

    public MinBalanceTransaction(final CommandInput command, final ArrayNode output, final User currentUser, final ClassicAccount account) {
        this.command = command;
        this.output = output;
        this.currentUser = currentUser;
        this.account = account;
        this.description = command.getDescription();
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
        if (currentUser == null || account == null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode balanceNode = mapper.createObjectNode();
            balanceNode.put("command", command.getCommand());
            balanceNode.put("timestamp", timestamp);
            balanceNode.put("error", "Invalid user");
        } else {
            account.setMinBalance(command.getAmount());
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
