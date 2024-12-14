package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.app.IBANRegistry;
import org.poo.fileio.CommandInput;

public class SetAliasTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    private ClassicAccount account;
    private IBANRegistry registry;

    public SetAliasTransaction(CommandInput command, IBANRegistry registry, ClassicAccount account) {
        this.command = command;
        this.registry = registry;
        this.account = account;
        this.description = command.getDescription();
        this.timestamp = command.getTimestamp();
    }

    public void makeTransaction() {
       if (!registry.updateAlias(account.getIban(), command.getAlias())) {
           System.out.println("Could not update the alias");
       }
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

    public ClassicAccount getAccount() {
        return account;
    }

    public void setAccount(ClassicAccount account) {
        this.account = account;
    }

    public IBANRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(IBANRegistry registry) {
        this.registry = registry;
    }
}
