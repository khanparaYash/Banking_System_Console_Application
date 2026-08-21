package factory;

import model.Account;

public abstract class AccountFactory {
    public abstract Account createAccount(int accountNumber, String holderName, String password);
}