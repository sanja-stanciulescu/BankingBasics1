package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class DeleteAccountTransaction implements TransactionStrategy{
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    private User currentUser;
    private ArrayNode output;

    public DeleteAccountTransaction(final CommandInput command, final ArrayNode output, final User currentUser) {
        this.command = command;
        this.currentUser = currentUser;
        this.output = output;
        this.description = command.getDescription();
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cardNode = mapper.createObjectNode();
        cardNode.put("command", command.getCommand());
        cardNode.put("timestamp", timestamp);
        ObjectNode outputNode = mapper.createObjectNode();
        int idx = searchAccount(currentUser, command.getAccount());
        if (idx == -1) {
            outputNode.put("error", "User and account don't match");
        } else {
            ClassicAccount wantedAccount = currentUser.getAccounts().get(idx);
            if (wantedAccount.getBalance() == 0) {
                wantedAccount.getCards().clear();
                currentUser.getAccounts().remove(idx);
                outputNode.put("success", "Account deleted");
                outputNode.put("timestamp", timestamp);
            } else {
                outputNode.put("error", "Balance should be 0");
                outputNode.put("timestamp", timestamp);
            }
        }
        cardNode.set("output", outputNode);
        output.add(cardNode);
    }

    private int searchAccount(final User user, final String iban) {
        if (user == null) {
            return -1;
        }

        for (int i = 0; i < user.getAccounts().size(); i++) {
            if (user.getAccounts().get(i).getIban().equals(iban)) {
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
