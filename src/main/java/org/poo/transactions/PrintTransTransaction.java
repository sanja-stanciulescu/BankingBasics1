package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class PrintTransTransaction implements TransactionStrategy {
    private CommandInput command;
    private int timestamp;
    private User user;
    private ArrayNode output;

    public PrintTransTransaction(CommandInput command, ArrayNode output, User user) {
        this.command = command;
        this.user = user;
        this.output = output;
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode transactionNode = mapper.createObjectNode();
        transactionNode.put("command", command.getCommand());
        transactionNode.put("timestamp", timestamp);

        ArrayNode printNode = mapper.createArrayNode();
        for (TransactionStrategy transaction : user.getTransactions()) {
            ObjectNode node = mapper.convertValue(transaction, ObjectNode.class);
            printNode.add(node);
        }
        transactionNode.set("output", printNode);
        output.add(transactionNode);
    }

    public CommandInput getCommand() {
        return command;
    }

    public void setCommand(CommandInput command) {
        this.command = command;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public void setOutput(ArrayNode output) {
        this.output = output;
    }
}
