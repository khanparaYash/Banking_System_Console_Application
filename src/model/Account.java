package model;
import module java.base;

public abstract class Account {
    private final int accountNumber;
    private final String holderName;
    private String password;
    private double balance;
    private final List<Transaction> transections;



    public Account(int accountNumber, String holderName, String password) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.password = password;
        this.balance = 0;
        this.transections = new ArrayList<>();
    }

    public String getHolderName() {
        return holderName;
    }

    public String getPassword() {
        return password;
    }

    public abstract String getAccountType();

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
    public abstract void  deposit(double amount, boolean showMessage);

    public abstract void withdraw(double amount, boolean showMessage);

    public List<Transaction> getTransections() {
        return transections;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(accountNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Account other)) {
            return false;
        }
        return this.accountNumber == other.accountNumber;
    }


}
