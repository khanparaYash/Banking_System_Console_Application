package factory;

import model.Account;
import model.CurrentAccount;

public class CurrentAccountFactory extends AccountFactory {
    @Override
    public Account createAccount(int accountNumber, String holderName, String password) {
        return new CurrentAccount(accountNumber, holderName, password);
    }
}