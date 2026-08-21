package model;

import exception.InsufficientBalanceException;

import util.Color;

public class SavingAccount extends Account {

    public SavingAccount(int accountNumber, String holderName, String password) {
        super(accountNumber, holderName, password);
    }

    @Override
    public synchronized void deposit(double amount, boolean showMessage) {

        setBalance(getBalance() + amount);
        String transactionType = showMessage ? "Deposit" : "Credit";
        getTransections().add(new Transaction(transactionType, amount, getBalance()));
        if (showMessage) {
            Color.colorPrint("Deposit Successfully Done", Color.BRIGHT_GREEN + Color.BOLD);
            Color.colorPrint(" Added Amount: " + amount, Color.BRIGHT_GREEN);
            Color.colorPrint(" Updated Balance: $" + getBalance(), Color.CYAN);
        }

    }


    @Override
    public String getAccountType() {
        return "Saving";
    }

    @Override
    public synchronized void withdraw(double amount, boolean showMessage) throws InsufficientBalanceException {
        if (getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient Balance");
        }
        setBalance(getBalance() - amount);
        String transactionType = showMessage ? "Withdraw" : "Debit";
        getTransections().add(new Transaction(transactionType, amount, getBalance()));
        if (showMessage) {
            Color.colorPrint("WithDraw Successfully Done", Color.BRIGHT_GREEN + Color.BOLD);
            Color.colorPrint(" Amount Deducted: " + amount, Color.BRIGHT_RED);
            Color.colorPrint(" Updated Balance: $" + getBalance(), Color.CYAN);

        }
    }
}
