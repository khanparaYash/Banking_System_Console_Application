package repo;

import model.Account;
import module java.base;
public class AccountRepo {

    private final Map<Integer, Account> accounts = new HashMap<>();
    private static  AccountRepo instance;// Singleton instance
    private AccountRepo() {
    }
    public static AccountRepo getInstance() {
        if (instance == null) {
            synchronized (AccountRepo.class){
                if (instance == null) {
                    instance = new AccountRepo();
                }
            }
        }
        return instance;
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts.values());

    }
    public int save(int AccountNumber, Account account) {
        accounts.put(AccountNumber, account);
        return AccountNumber;
    }

    public Account findByAccountNumber(int accountNumber) {
        return accounts.get(accountNumber);
    }
}
