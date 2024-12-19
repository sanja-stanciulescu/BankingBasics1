package org.poo.app;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.transactions.*;
import org.poo.users.User;

import java.util.ArrayList;

public class AppManager {
    private ArrayList<User> allUsers;
    private Bnr bank;
    private IBANRegistry registry;
    private Finder finder;

    public AppManager() {
        allUsers = new ArrayList<>();
        bank = new Bnr();
        registry = new IBANRegistry();
        finder = new Finder();
    }

    public void start(final ArrayNode output, final ObjectInput inputData) {
        //Initialize the list of users
        for (int i = 0; i < inputData.getUsers().length; i++) {
            allUsers.add(new User(inputData.getUsers()[i]));
        }

        //Initialize the board where exchange rates are showcased
        bank.setUp(inputData);

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
                searchUserByEmail(command.getEmail());
                transaction = new AddAccountTransaction(command, registry, finder.getUser());
                break;
            case "createCard", "createOneTimeCard":
                searchUserByEmail(command.getEmail());
                transaction = new AddCardTransaction(command, finder.getUser());
                break;
            case "addFunds":
                searchByIban(command.getAccount());
                transaction = new AddFundsTransaction(command, finder.getAccount());
                break;
            case "deleteAccount":
                searchUserByEmail(command.getEmail());
                transaction = new DeleteAccountTransaction(command, output, finder.getUser());
                break;
            case "deleteCard":
                searchByCard(command.getCardNumber());
                transaction = new DeleteCardTransaction(command, finder.getAccount(), finder.getUser());
                break;
            case "setMinimumBalance":
                searchByIban(command.getAccount());
                transaction = new MinBalanceTransaction(command, output, finder.getUser(), finder.getAccount());
                break;
            case "payOnline":
                searchUserByEmail(command.getEmail());
                transaction = new PayOnlineTransaction(command, output, bank, finder.getUser());
                break;
            case "sendMoney":
                searchByIban(command.getAccount());
                currentAccount = finder.getAccount();
                currentUser = finder.getUser();
                searchByIban(registry.getIBAN(command.getReceiver()));
                transaction = new SendMoneyTransaction(command, currentAccount, currentUser, finder.getAccount(), finder.getUser(), bank);
                break;
            case "setAlias":
                searchByIban(command.getAccount());
                transaction = new SetAliasTransaction(command, registry, finder.getAccount());
                break;
            case "printTransactions":
                searchUserByEmail(command.getEmail());
                transaction = new PrintTransTransaction(command, output, finder.getUser());
                break;
            case "checkCardStatus":
                searchByCard(command.getCardNumber());
                transaction = new CheckCardStatusTransaction(command, output, finder.getUser(), finder.getAccount(), finder.getCard());
                break;
            case "changeInterestRate":
                searchByIban(command.getAccount());
                transaction = new ChangeInterestTransaction(command, output, finder.getUser(), finder.getAccount());
                break;
            case "addInterest":
                searchByIban(command.getAccount());
                transaction = new AddInterestTransaction(command, output, finder.getAccount());
                break;
            case "splitPayment":
                ArrayList<Finder> finders = new ArrayList<>();
                for (int i = 0; i < command.getAccounts().size(); i++) {
                    finders.add(new Finder());
                    searchByIban(command.getAccounts().get(i));
                    finders.get(i).setUser(finder.getUser());
                    finders.get(i).setAccount(finder.getAccount());
                    finders.get(i).setCard(finder.getCard());
                }
                transaction = new SplitPaymentTransaction(command, finders, bank);
                break;
            case "report":
                searchByIban(command.getAccount());
                transaction = new ReportTransaction(command, output, finder.getAccount());
                break;
            case "spendingsReport":
                searchByIban(command.getAccount());
                transaction = new SpendingsReportTransaction(command, output, finder.getAccount());
                break;
            default:
                System.out.println("Invalid command");
        }

        return transaction;
    }

    private void searchUserByEmail(final String email) {
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                finder.setUser(user);
                return;
            }
        }
        finder.setUser(null);
    }

    private void searchByIban(final String iban) {
        for (User user : allUsers) {
            for (ClassicAccount account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    finder.setUser(user);
                    finder.setAccount(account);
                    return;
                }
            }
        }

        finder.setUser(null);
        finder.setAccount(null);
    }

    private void searchByCard(String cardNumber) {
        for (User user : allUsers) {
            for (ClassicAccount account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        finder.setUser(user);
                        finder.setAccount(account);
                        finder.setCard(card);
                        return;
                    }
                }
            }
        }

        finder.setUser(null);
        finder.setAccount(null);
        finder.setCard(null);
    }

    public ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(ArrayList<User> allUsers) {
        this.allUsers = allUsers;
    }
}
