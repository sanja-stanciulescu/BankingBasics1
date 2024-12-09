package org.poo.app;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.ClassicAccount;
import org.poo.commerciants.CommerciantCatgeory;
import org.poo.exchangeRates.ExchangeRate;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.transactions.*;
import org.poo.users.User;

import java.util.ArrayList;

public class AppManager {
    private ArrayList<User> allUsers;
    private ArrayList<ExchangeRate> exchangeRates;
    private ArrayList<CommerciantCatgeory> commerciantCategories;

    public AppManager() {
        allUsers = new ArrayList<>();
        exchangeRates = new ArrayList<>();
        commerciantCategories = new ArrayList<>();
    }

    public void start(final ArrayNode output, final ObjectInput inputData) {
        //Initialize the list of users
        for (int i = 0; i < inputData.getUsers().length; i++) {
            allUsers.add(new User(inputData.getUsers()[i]));
        }

        int length;

        //Initialize the board where exchange rates are showcased
        if (inputData.getExchangeRates() != null) {
            length = inputData.getExchangeRates().length;
        } else {
            length = 0;
        }
        for (int i = 0; i < length; i++) {
            exchangeRates.add(new ExchangeRate(inputData.getExchangeRates()[i]));
        }

        //Initialize possible categories for commerciants
        if (inputData.getCommerciants() != null) {
            length = inputData.getCommerciants().length;
        } else {
            length = 0;
        }
        for (int i = 0; i < length; i++) {
            commerciantCategories.add(new CommerciantCatgeory(inputData.getCommerciants()[i]));
        }

        //Parse the commands
        CommandInput[] commands = inputData.getCommands();
        TransactionStrategy transaction;
        for (CommandInput command : commands) {
            transaction = useTransactionFactory(output, command);
            if (transaction != null) {
                transaction.makeTransaction();
            }
        }
    }

    public TransactionStrategy useTransactionFactory(final ArrayNode output, final CommandInput command) {
        TransactionStrategy transaction = null;
        User currentUser;
        ClassicAccount currentAccount;

        switch (command.getCommand()) {
            case "printUsers":
                transaction = new PrintUserTransaction(command, output, allUsers);
                break;
            case "addAccount":
                currentUser = searchUserByEmail(command.getEmail());
                transaction = new AddAccountTransaction(command, currentUser);
                break;
            case "createCard":
                currentUser = searchUserByEmail(command.getEmail());
                transaction = new AddCardTransaction(command, currentUser);
                break;
            case "addFunds":
                currentAccount = searchAccountByIban(command.getAccount());
                transaction = new AddFundsTransaction(command, currentAccount);
                break;
            default:
                System.out.println("Invalid command");
        }

        return transaction;
    }

    private User searchUserByEmail(final String email) {
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    private ClassicAccount searchAccountByIban(final String iban) {
        for (User user : allUsers) {
            for (ClassicAccount account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return account;
                }
            }
        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(ArrayList<User> allUsers) {
        this.allUsers = allUsers;
    }

    public ArrayList<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(ArrayList<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public ArrayList<CommerciantCatgeory> getCommerciantCategories() {
        return commerciantCategories;
    }

    public void setCommerciantCategories(ArrayList<CommerciantCatgeory> commerciantCategories) {
        this.commerciantCategories = commerciantCategories;
    }
}
