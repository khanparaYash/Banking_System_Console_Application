package model;

import exception.InsufficientBalanceException;

import util.Color;

public class CurrentAccount extends Account {

    public CurrentAccount(int accountNumber, String holderName, String password) {
        super(accountNumber, holderName, password);
    }

    @Override
    public String getAccountType() {
        return "Current";
    }

    @Override
    public synchronized void withdraw(double amount, boolean showMessage) throws InsufficientBalanceException {
        int overdraftLimit = 2000;
        if (getBalance() - amount < (-overdraftLimit)) {
            throw new InsufficientBalanceException("Insufficient Balance");
        }

        setBalance(getBalance() - amount);
        String transactionType = showMessage ? "Withdraw" : "Debit";
        getTransections().add(new Transaction(transactionType, amount, getBalance()));
        if (showMessage) {
            Color.colorPrint("Withdraw Successfully Done", Color.BRIGHT_GREEN + Color.BOLD);
            Color.colorPrint(" Amount Deducted: " + amount, Color.BRIGHT_RED);
            Color.colorPrint(" Updated Balance: $" + getBalance(), Color.CYAN);

        }

    }

    @Override
    public synchronized void deposit(double amount, boolean showMessage) {
        setBalance(getBalance() + amount);
        String transactionType = showMessage ? "Deposit" : "Credit";
        getTransections().add(new Transaction(transactionType, amount, getBalance()));
        if (showMessage) {
            Color.colorPrint("Deposit Successfully Done", Color.BRIGHT_GREEN + Color.BOLD);
            Color.colorPrint(" Amount Added: " + amount, Color.BRIGHT_GREEN);
            Color.colorPrint(" Updated Balance: $" + getBalance(), Color.CYAN);
        }

    }
}
