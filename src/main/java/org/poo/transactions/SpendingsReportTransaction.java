package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;

public class SpendingsReportTransaction implements TransactionStrategy {
    private CommandInput command;
    private ClassicAccount account;
    private ArrayNode output;
    private int timestamp;

    public SpendingsReportTransaction(CommandInput command, ArrayNode output, ClassicAccount account) {
        this.command = command;
        this.output = output;
        this.account = account;
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
        if (account == null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("command", command.getCommand());
            node.put("timestamp", timestamp);

            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("description", "Account not found");
            errorNode.put("timestamp", timestamp);

            node.set("output", errorNode);
            output.add(node);

            return;
        }
        ObjectNode node = ReportTransaction.gatherData(command, account);
        output.add(node);
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
