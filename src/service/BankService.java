package service;

import enums.AccountTypeEnum;
import exception.AccountNotFoundException;
import exception.AuthenticationException;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import factory.AccountFactory;
import factory.CurrentAccountFactory;
import factory.SavingAccountFactory;
import model.Account;

import model.Transaction;
import repo.AccountRepo;
import util.AccountNumberGenerator;
import util.Color;

import java.util.Comparator;

import module java.base;

import static java.lang.IO.println;
import static util.SimulateProcessing.AddWaiting;

public class BankService {

    private static  BankService INSTANCE;

    private BankService() {
    }

    public static BankService getInstance() {
        if(INSTANCE == null) {
            synchronized (BankService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BankService();
                }
            }
        }
        return INSTANCE;
    }

    private final AccountRepo accountRepo = AccountRepo.getInstance();

    public int createAccount(String holderName, String password, AccountTypeEnum type) throws IllegalArgumentException {
        if (holderName == null || holderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Holder name cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        int accountNumber = AccountNumberGenerator.generate();
        AddWaiting("Creating account");

        AccountFactory factory = switch (type) {
            case SAVING -> new SavingAccountFactory();
            case CURRENT -> new CurrentAccountFactory();
        };

        Account account = factory.createAccount(accountNumber, holderName, password);
        return accountRepo.save(accountNumber, account);
    }

    public Account login(int accountNumber, String password) throws AccountNotFoundException, AuthenticationException {
        AddWaiting("verifying account");
        Account account = accountRepo.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        } else if (account.getPassword().equals(password)) {
            return account;
        }
        throw new AuthenticationException("Wrong password");
    }

    public void deposit(Account account, double amount) throws InvalidAmountException {
        AddWaiting("Transacting in Process");
        if (amount <= 0) {
            throw new InvalidAmountException("Invalid amount");
        }
        account.deposit(amount, true);
    }

    public void withdraw(Account account, double amount) throws InvalidAmountException {
        AddWaiting("Transacting in Process");
        if (amount <= 0) {
            throw new InvalidAmountException("Invalid amount");
        }
        account.withdraw(amount, true);

    }

    public void transfer(Account sourceAccount, int targetAccountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException {
        AddWaiting("Transacting in Process");
        if (amount <= 0) {
            throw new InvalidAmountException("Invalid amount");
        }

        Account targetAccount = accountRepo.findByAccountNumber(targetAccountNumber);
        if (targetAccount == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if (sourceAccount.getAccountNumber() == targetAccount.getAccountNumber()) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        sourceAccount.withdraw(amount, false);
        targetAccount.deposit(amount, false);
        Color.colorPrint("Transfer Successfully Done", Color.BRIGHT_GREEN + Color.BOLD);
        Color.colorPrint("Transferred Amount: $" + amount, Color.BRIGHT_GREEN);
        Color.colorPrint("Recipient Account: " + targetAccountNumber, Color.CYAN);
    }

    public double showBalance(Account account) {
        AddWaiting("Fetching balance");
        return account.getBalance();
    }

    public void showHistory(Account account) {
        AddWaiting("Fetching history");
        println("==============================================================");
        System.out.printf("%-12s %-10s %-12s %-20s%n", "TYPE", "AMOUNT", "BALANCE", "DATE & TIME");
        println("==============================================================");
        for (Transaction transaction : account.getTransections()) {
            println(transaction);
        }
    }

    public void ChangePassword(Account account, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        account.setPassword(newPassword);
        AddWaiting("Changing Password");
        Color.colorPrint("Password changed successfully.", Color.BRIGHT_GREEN + Color.BOLD);
    }

    public double getTotalBankHoldings() {
        List<Account> accountFactories = accountRepo.getAccounts();
        return accountFactories.stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    public List<String> getTop3RichestAccountHolders() {
        List<Account> accountFactories = accountRepo.getAccounts();
        return accountFactories.stream()
                .sorted(Comparator.comparing(Account::getBalance).reversed())
                .limit(3)
                .map(Account::getHolderName)
                .collect(Collectors.toList());
    }

    public List<Double> getTop5Balances() {
        List<Account> accountFactories = accountRepo.getAccounts();
        return accountFactories.stream()
                .map(Account::getBalance)
                .sorted(Comparator.reverseOrder())
                .limit(5)
                .collect(Collectors.toList());
    }

    public Map<String, Long> countAccountsByType() {
        List<Account> accountFactories = accountRepo.getAccounts();
        return accountFactories.stream()
                .collect(Collectors.groupingBy(
                        Account::getAccountType,
                        Collectors.counting()
                ));
    }
}
