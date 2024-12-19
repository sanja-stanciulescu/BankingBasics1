package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class ReportTransaction implements TransactionStrategy {
    private CommandInput command;
    private ClassicAccount account;
    private ArrayNode output;
    private int timestamp;

    public ReportTransaction(CommandInput command, ArrayNode output, ClassicAccount account) {
        this.command = command;
        this.output = output;
        this.account = account;
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
        if (account == null) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("command", command.getCommand());

        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("IBAN", account.getIban());

        ArrayNode transactionsNode = mapper.createArrayNode();
        for (TransactionStrategy transaction : account.getTransactions()) {
            if (transaction.getTimestamp() >= command.getStartTimestamp() && transaction.getTimestamp() <= command.getEndTimestamp()) {
                ObjectNode transNode = mapper.convertValue(transaction, ObjectNode.class);
                transactionsNode.add(transNode);
            }
        }
        outputNode.set("transactions", transactionsNode );

        node.set("output", outputNode);
        output.add(node);
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
