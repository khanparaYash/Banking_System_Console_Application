package factory;

import model.Account;
import model.SavingAccount;

public class SavingAccountFactory extends AccountFactory {
    @Override
    public Account createAccount(int accountNumber, String holderName, String password) {
        return new SavingAccount(accountNumber, holderName, password);
    }
}