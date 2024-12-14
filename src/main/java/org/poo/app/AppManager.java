package org.poo.app;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.commerciants.CommerciantCatgeory;
import org.poo.exchangeRates.Bnr;
import org.poo.exchangeRates.ExchangeRate;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.transactions.*;
import org.poo.users.User;

import java.util.ArrayList;

public class AppManager {
    private ArrayList<User> allUsers;
    private Bnr bank;
    private ArrayList<CommerciantCatgeory> commerciantCategories;
    private IBANRegistry registry;

    public AppManager() {
        allUsers = new ArrayList<>();
        bank = new Bnr();
        commerciantCategories = new ArrayList<>();
        registry = new IBANRegistry();
    }

    public void start(final ArrayNode output, final ObjectInput inputData) {
        //Initialize the list of users
        for (int i = 0; i < inputData.getUsers().length; i++) {
            allUsers.add(new User(inputData.getUsers()[i]));
        }

        //Initialize the board where exchange rates are showcased
        bank.setUp(inputData);

        //Initialize possible categories for commerciants
        int length;
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
        Card currentCard;
        switch (command.getCommand()) {
            case "printUsers":
                transaction = new PrintUserTransaction(command, output, allUsers);
                break;
            case "addAccount":
                currentUser = searchUserByEmail(command.getEmail());
                transaction = new AddAccountTransaction(command, registry, currentUser);
                break;
            case "createCard", "createOneTimeCard":
                currentUser = searchUserByEmail(command.getEmail());
                transaction = new AddCardTransaction(command, currentUser);
                break;
            case "addFunds":
                currentAccount = searchAccountByIban(command.getAccount());
                transaction = new AddFundsTransaction(command, currentAccount);
                break;
            case "deleteAccount":
                currentUser = searchUserByEmail(command.getEmail());
                transaction = new DeleteAccountTransaction(command, output, currentUser);
                break;
            case "deleteCard":
                currentAccount = searchAccountByCard(command.getCardNumber());
                if (currentAccount != null) {
                    currentUser = searchUserByIban(currentAccount.getIban());
                } else {
                    currentUser = null;
                }
                transaction = new DeleteCardTransaction(command, currentAccount, currentUser);
                break;
            case "setMinimumBalance":
                currentUser = searchUserByIban(command.getAccount());
                transaction = new MinBalanceTransaction(command, output, currentUser);
                break;
            case "payOnline":
                currentUser = searchUserByEmail(command.getEmail());
                transaction = new PayOnlineTransaction(command, output, bank, currentUser);
                break;
            case "sendMoney":
                currentAccount = searchAccountByIban(command.getAccount());
                ClassicAccount receiver = searchAccountByIban(registry.getIBAN(command.getReceiver()));
                transaction = new SendMoneyTransaction(command, currentAccount, receiver, bank);
                break;
            case "setAlias":
                currentAccount = searchAccountByIban(command.getAccount());
                transaction = new SetAliasTransaction(command, registry, currentAccount);
                break;
            case "printTransactions":
                currentUser = searchUserByEmail(command.getEmail());
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

    private User searchUserByIban(final String iban) {
        for (User user : allUsers) {
            for (ClassicAccount account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return user;
                }
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

    private ClassicAccount searchAccountByCard(String cardNumber) {
        for (User user : allUsers) {
            for (ClassicAccount account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return account;
                    }
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

    public ArrayList<CommerciantCatgeory> getCommerciantCategories() {
        return commerciantCategories;
    }

    public void setCommerciantCategories(ArrayList<CommerciantCatgeory> commerciantCategories) {
        this.commerciantCategories = commerciantCategories;
    }
}
