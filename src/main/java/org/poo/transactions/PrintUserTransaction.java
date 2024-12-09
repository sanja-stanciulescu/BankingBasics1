package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

import java.util.ArrayList;

public class PrintUserTransaction implements TransactionStrategy{
    private String command;
    private int timestamp;
    private ArrayList<User> allUsers;
    private ArrayNode output;

    public PrintUserTransaction(final CommandInput command, final ArrayNode output, final ArrayList<User> allUsers) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.output = output;
        this.allUsers = allUsers;
    }

    @Override
    public void makeTransaction() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode printUsersNode = mapper.createObjectNode();
        printUsersNode.put("command", command);
        printUsersNode.put("timestamp", timestamp);

        ArrayNode usersArray = mapper.createArrayNode();
        for (User user : allUsers) {
            ObjectNode userNode = mapper.convertValue(user, ObjectNode.class);
            usersArray.add(userNode);
        }
        printUsersNode.set("output", usersArray);
        output.add(printUsersNode);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public void setOutput(ArrayNode output) {
        this.output = output;
    }

    public ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(ArrayList<User> allUsers) {
        this.allUsers = allUsers;
    }
}
