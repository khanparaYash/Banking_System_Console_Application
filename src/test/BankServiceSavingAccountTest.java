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

@DisplayName("BankService unit tests for SavingAccount")
public class BankServiceSavingAccountTest {

    private BankService bankService;

    //clears accounts Map before each test method.
    @BeforeEach
    void setUp() throws Exception {
        bankService = BankService.getInstance();
        AccountRepo repo = AccountRepo.getInstance();
        Field accountsField = AccountRepo.class.getDeclaredField("accounts");
        accountsField.setAccessible(true);
        Map<?, ?> accounts = (Map<?, ?>) accountsField.get(repo);
        accounts.clear();
    }

    //  Creates a new saving account for testing.
    public int creatAccount() {
        return bankService.createAccount("Yash", "pwd123", AccountTypeEnum.SAVING);
    }


    // this testcase is defined for creating saving account and login with correct password.
    @Test
    @DisplayName("create saving account and login with correct password")
    void createAccountAndLogin_success() throws Exception {
        int accNo = creatAccount();
        Account account = bankService.login(accNo, "pwd123");

        assertNotNull(account);
        assertEquals("Yash", account.getHolderName());
        assertEquals("Saving", account.getAccountType());
        assertEquals(0.0, account.getBalance(), 0.0001);
    }


    //this testcase is defined for testing login with wrong password for saving account.
    @Test
    @DisplayName("login fails with wrong password for saving account")
    void login_wrongPassword_throws() throws Exception {
        int accNo = creatAccount();
        assertThrows(AuthenticationException.class, () -> bankService.login(accNo, "ABCD"));
    }

    // this testcase is defined for testing login for non-existent saving account.
    @Test
    @DisplayName("login fails for non-existent saving account")
    void login_accountNotFound_throws() {
        assertThrows(AccountNotFoundException.class, () -> bankService.login(123456789, "ABCD"));
    }


    //this testcase is defined for testing deposit of valid amount for saving account.
    @Test
    @DisplayName("deposit valid amount for saving account")
    void deposit_validAmount_increasesBalance() throws Exception {
        int accNo = creatAccount();

        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);
        assertNotNull(account);

        // Add some balance to the account
        bankService.deposit(account, 150.0);
        // Verify its updated
        assertEquals(150.0, account.getBalance(), 0.0001);
    }

    //this testcase is defined for testing deposit of invalid amount for saving account.
    @Test
    @DisplayName("deposit rejects zero and negative amount for saving account")
    void deposit_invalidAmount_throws() throws Exception {
        int accNo = creatAccount();
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);
        assertNotNull(account);
        // Add some balance first
        bankService.deposit(account, 150.0);
        assertEquals(150.0, account.getBalance(), 0.0001);

        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, 0));
        // Balance should remain unchanged after failed deposit
        assertEquals(150.0, account.getBalance(), 0.0001);

        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, -10));
        // Balance should remain unchanged after failed deposit
        assertEquals(150.0, account.getBalance(), 0.0001);
    }


    //this testcase is defined for testing withdrawal succeeds when sufficient balance for saving account.
    @Test
    @DisplayName("withdraw succeeds when sufficient balance for saving account")
    void withdraw_validAmount_success() throws Exception {
        int accNo = creatAccount();
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        // Add some balance to the account
        bankService.deposit(account, 200.0);
        // Verify its updated
        assertEquals(200.0, account.getBalance(), 0.0001);

        // Withdraw a valid amount from the account
        bankService.withdraw(account, 50.0);
        // Verify its updated
        assertEquals(150.0, account.getBalance(), 0.0001);
    }


    // this testcase is defined for testing withdrawal of invalid amount for saving account.
    @Test
    @DisplayName("withdraw rejects zero and negative amount for saving account")
    void withdraw_invalidAmount_throws() throws Exception {
        int accNo = creatAccount();
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);
        assertNotNull(account);

        // Add some balance
        bankService.deposit(account, 200.0);
        // Verify its updated
        assertEquals(200.0, account.getBalance(), 0.0001);

        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, 0));
        // The balance should remain unchanged after the failed withdrawal attempt.
        assertEquals(200.0, account.getBalance(), 0.0001);

        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, -5));
        // The balance should remain unchanged after the failed withdrawal attempt.
        assertEquals(200.0, account.getBalance(), 0.0001);
    }


    // this testcase is defined for testing withdrawal fails when insufficient balance for saving account.
    @Test
    @DisplayName("withdraw fails when insufficient balance for saving account")
    void withdraw_insufficientBalance_throws() throws Exception {
        int accNo = creatAccount();
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);
        assertNotNull(account);

        // Add some balance to the account
        bankService.deposit(account, 30.0);
        // Verify its updated
        assertEquals(30.0, account.getBalance(), 0.0001);

        // Attempt to withdraw more than the available balance
        assertThrows(InsufficientBalanceException.class, () -> bankService.withdraw(account, 100.0));
        // The balance should remain unchanged after the failed withdrawal attempt.
        assertEquals(30.0, account.getBalance(), 0.0001);
    }
}
