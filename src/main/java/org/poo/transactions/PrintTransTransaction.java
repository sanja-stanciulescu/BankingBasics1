package org.poo.transactions;

import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class PrintTransTransaction implements TransactionStrategy {
    private CommandInput command;
    private int timestamp;
    private User user;

    public PrintTransTransaction(CommandInput command, User user) {
        this.command = command;
        this.user = user;
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {

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
}
