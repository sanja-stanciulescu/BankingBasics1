package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

        ObjectNode node = gatherData(command, account);
        output.add(node);
    }

    public static ObjectNode gatherData(CommandInput command, ClassicAccount account) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", command.getTimestamp());
        node.put("command", command.getCommand());

        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("IBAN", account.getIban());

        ArrayNode transactionsNode = mapper.createArrayNode();
        TreeMap<String, Double> sortedCommerciants = new TreeMap<>();

        if (command.getCommand().equals("report")) {
            for (TransactionStrategy transaction : account.getTransactions()) {
                if (transaction.getTimestamp() >= command.getStartTimestamp() && transaction.getTimestamp() <= command.getEndTimestamp()) {
                    ObjectNode transNode = mapper.convertValue(transaction, ObjectNode.class);
                    transactionsNode.add(transNode);
                }
            }
        } else {
            for (PayOnlineTransaction transaction : account.getCommerciants().getPayments()) {
                if (transaction.getTimestamp() >= command.getStartTimestamp() && transaction.getTimestamp() <= command.getEndTimestamp()) {
                    ObjectNode transNode = mapper.convertValue(transaction, ObjectNode.class);
                    transactionsNode.add(transNode);

                    sortedCommerciants.put(transaction.getCommerciant(), sortedCommerciants.getOrDefault(transaction.getCommerciant(), 0.0) + transaction.getAmount());
                }
            }
        }
        outputNode.set("transactions", transactionsNode );

        if (command.getCommand().equals("spendingsReport")) {
            ArrayNode commerciantsNode = mapper.createArrayNode();
            for (Map.Entry<String, Double> entry : sortedCommerciants.entrySet()) {
                ObjectNode payNode = mapper.createObjectNode();
                payNode.put("commerciant", entry.getKey());
                payNode.put("total", entry.getValue());
                commerciantsNode.add(payNode);
            }
            outputNode.set("commerciants", commerciantsNode);
        }

        node.set("output", outputNode);

        return node;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
