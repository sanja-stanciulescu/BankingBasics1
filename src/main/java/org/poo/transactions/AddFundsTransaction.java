package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;

public class AddFundsTransaction implements TransactionStrategy{
    private int timestamp;

    @JsonIgnore
    private ClassicAccount currentAccount;
    private CommandInput command;

    public AddFundsTransaction(CommandInput command, ClassicAccount currentAccount) {
        this.currentAccount = currentAccount;
        this.command = command;
        timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
        currentAccount.setBalance(currentAccount.getBalance() + command.getAmount());
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
