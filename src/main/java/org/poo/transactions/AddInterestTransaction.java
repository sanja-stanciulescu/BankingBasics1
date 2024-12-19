package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;

public class AddInterestTransaction implements TransactionStrategy {
    private CommandInput command;
    private ClassicAccount account;
    private ArrayNode output;
    private int timestamp;

    public AddInterestTransaction(CommandInput command, ArrayNode output, ClassicAccount account) {
        this.command = command;
        this.output = output;
        this.account = account;
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
        if (account == null) {
            System.out.println("Could not change interest rate");
        }
        if (account.getType().equals("classic")) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("command", command.getCommand());
            node.put("timestamp", timestamp);

            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("description", "This is not a savings account");
            errorNode.put("timestamp", timestamp);

            node.set("output", errorNode);
            output.add(node);

            return;
        }
        account.addInterest();
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
