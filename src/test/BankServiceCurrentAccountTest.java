package test;

import enums.AccountTypeEnum;
import exception.AccountNotFoundException;
import exception.AuthenticationException;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repo.AccountRepo;
import service.BankService;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BankService unit tests for CurrentAccount")
public class BankServiceCurrentAccountTest {

    private BankService bankService;

    /**
     * clears accounts Map before each test method.
     */
    @BeforeEach
    void setUp() throws Exception {
        bankService = BankService.getInstance();
        AccountRepo repo = AccountRepo.getInstance();
        Field accountsField = AccountRepo.class.getDeclaredField("accounts");
        accountsField.setAccessible(true);
        Map<?, ?> accounts = (Map<?, ?>) accountsField.get(repo);
        accounts.clear();
    }

    //Creates a new current account for testing.
    public int creatAccount() {
        return bankService.createAccount("Carl", "pwd", AccountTypeEnum.CURRENT);
    }


    //this testcase is defined for creating current account and login with correct password.
    @Test
    @DisplayName("create current account and login with correct password")
    void createAccountAndLogin_success() throws Exception {
        int accNo = creatAccount();
        Account account = bankService.login(accNo, "pwd");

        assertNotNull(account);
        assertEquals("Carl", account.getHolderName());
        assertEquals("Current", account.getAccountType());
        assertEquals(0.0, account.getBalance(), 0.0001);
    }

    //this testcase is defined for testing login with wrong password for current account.
    @Test
    @DisplayName("login fails with wrong password for current account")
    void login_wrongPassword_throws() throws Exception {
        int accNo = creatAccount();
        assertThrows(AuthenticationException.class, () -> bankService.login(accNo, "wrong"));
    }

    // this testcase is defined for testing login for non-existent current account.
    @Test
    @DisplayName("login fails for non-existent current account")
    void login_accountNotFound_throws() {
        assertThrows(AccountNotFoundException.class, () -> bankService.login(99999, "pwd"));
    }

    // this testcase is defined for testing deposit of valid amount for current account.
    @Test
    @DisplayName("deposit valid amount for current account")
    void deposit_validAmount_increasesBalance() throws Exception {
        int accNo = creatAccount();
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);
        assertNotNull(account);

        // Add some balance to the account
        bankService.deposit(account, 500.0);
        // Verify its updated
        assertEquals(500.0, account.getBalance(), 0.0001);
    }

    // this testcase is defined for testing deposit of invalid amount for current account.
    @Test
    @DisplayName("deposit rejects zero and negative amount for current account")
    void deposit_invalidAmount_throws() throws Exception {
        int accNo = creatAccount();
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        //Add some balance
        bankService.deposit(account, 100.0);
        //Verify its updated
        assertEquals(100.0, account.getBalance(), 0.0001);

        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, 0));
        // The balance should remain unchanged after the failed deposit attempt.
        assertEquals(100.0, account.getBalance(), 0.0001);

        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, -20));
        // The balance should remain unchanged after the failed deposit attempt.
        assertEquals(100.0, account.getBalance(), 0.0001);
    }

    // this testcase is defined for testing withdrawal within overdraft limit for current account.
    @Test
    @DisplayName("withdraw within overdraft limit succeeds for current account")
    void withdraw_withinOverdraft_success() throws Exception {
        int accNo = creatAccount();
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);
        assertNotNull(account);

        // Withdraw within the overdraft limit
        bankService.withdraw(account, 1500.0);
        // Verify its updated
        assertEquals(-1500.0, account.getBalance(), 0.0001);

        // Withdraw again within the overdraft limit
        bankService.withdraw(account, 500.0);
        // Verify its updated
        assertEquals(-2000.0, account.getBalance(), 0.0001);
    }

    // this testcase is defined for testing withdrawal at exact overdraft limit for current account.
    @Test
    @DisplayName("withdraw at exact overdraft limit is allowed")
    void withdraw_exactOverdraftLimit_success() throws Exception {
        int accNo = creatAccount();
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);
        assertNotNull(account);

        // Withdraw up to the exact overdraft limit
        bankService.withdraw(account, 2000.0);
        // Verify its updated
        assertEquals(-2000.0, account.getBalance(), 0.0001);
    }

    //this testcase is defined for testing withdrawal beyond overdraft limit for current account.
    @Test
    @DisplayName("withdraw beyond overdraft limit fails for current account")
    void withdraw_exceedsOverdraft_throws() throws Exception {
        int accNo = creatAccount();
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);
        assertNotNull(account);

        assertThrows(InsufficientBalanceException.class, () -> bankService.withdraw(account, 2001.0));
        // Balance should remain unchanged after the failed withdrawal attempt.
        assertEquals(0.0, account.getBalance(), 0.0001);
    }

    // this testcase is defined for testing withdrawal of invalid amount for current account.
    @Test
    @DisplayName("withdraw rejects zero and negative amount for current account")
    void withdraw_invalidAmount_throws() throws Exception {
        int accNo = creatAccount();
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);
        assertNotNull(account);

        // Add some balance
        bankService.deposit(account, 100.0);
        // Verify its updated
        assertEquals(100.0, account.getBalance(), 0.0001);

        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, 0));
        // The balance should remain unchanged after the failed withdrawal attempt.
        assertEquals(100.0, account.getBalance(), 0.0001);

        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, -1));
        // The balance should remain unchanged after the failed withdrawal attempt.
        assertEquals(100.0, account.getBalance(), 0.0001);
    }
}
