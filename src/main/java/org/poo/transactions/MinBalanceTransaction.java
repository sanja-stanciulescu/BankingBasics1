package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class MinBalanceTransaction implements TransactionStrategy{
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    private User currentUser;
    private ArrayNode output;

    public MinBalanceTransaction(final CommandInput command, final ArrayNode output, final User currentUser) {
        this.command = command;
        this.output = output;
        this.currentUser = currentUser;
        this.description = command.getDescription();
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
        if (currentUser == null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode balanceNode = mapper.createObjectNode();
            balanceNode.put("command", command.getCommand());
            balanceNode.put("timestamp", timestamp);
            balanceNode.put("error", "Invalid user");
        } else {
            int idx = searchAccount(command.getAccount());
            currentUser.getAccounts().get(idx).setMinBalance(command.getAmount());
        }
    }

    private int searchAccount(final String iban) {
        for (int i = 0; i < currentUser.getAccounts().size(); i++) {
            if (currentUser.getAccounts().get(i).getIban().equals(iban)) {
                return i;
            }
        }

        return -1;
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
