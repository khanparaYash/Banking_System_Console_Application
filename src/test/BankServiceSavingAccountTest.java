package test;

import enums.AccountTypeEnum;
import exception.AccountNotFoundException;
import exception.AuthenticationException;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import model.Account;
import repo.AccountRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.BankService;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BankService unit tests: login, deposit, withdraw")
public class BankServiceSavingAccountTest {

    private BankService bankService;

    @BeforeEach
    void setUp() throws Exception {
        bankService = BankService.getInstance();
        // Clear the AccountRepo internal storage so tests are isolated
        AccountRepo repo = AccountRepo.getInstance();
        Field accountsField = AccountRepo.class.getDeclaredField("accounts");
        accountsField.setAccessible(true);
        Map<?, ?> accounts = (Map<?, ?>) accountsField.get(repo);
        accounts.clear();
    }

    @Test
    @DisplayName("login succeeds with correct credentials")
    void loginSuccess() throws Exception {
        int acc = bankService.createAccount("Alice", "pwd123", AccountTypeEnum.SAVING);
        Account account = bankService.login(acc, "pwd123");
        assertNotNull(account);
        assertEquals("Alice", account.getHolderName());
        assertEquals(0.0, account.getBalance());
    }

    @Test
    @DisplayName("login fails with wrong password")
    void loginWrongPassword() throws Exception {
        int acc = bankService.createAccount("Bob", "secret", AccountTypeEnum.SAVING);
        assertThrows(AuthenticationException.class, () -> bankService.login(acc, "badpass"));
    }

    @Test
    @DisplayName("login fails for non-existent account")
    void loginAccountNotFound() {
        assertThrows(AccountNotFoundException.class, () -> bankService.login(123456789, "nopass"));
    }

    @Test
    @DisplayName("deposit increases balance for valid amount")
    void depositValid() throws Exception {
        int acc = bankService.createAccount("Carol", "p", AccountTypeEnum.SAVING);
        Account account = AccountRepo.getInstance().findByAccountNumber(acc);
        bankService.deposit(account, 150.0);
        assertEquals(150.0, account.getBalance(), 0.0001);
    }

    @Test
    @DisplayName("deposit rejects non-positive amount")
    void depositInvalidAmount() throws Exception {
        int acc = bankService.createAccount("Dan", "p", AccountTypeEnum.SAVING);
        Account account = AccountRepo.getInstance().findByAccountNumber(acc);
        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, 0));
        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, -10));
    }

    @Test
    @DisplayName("withdraw succeeds when sufficient balance")
    void withdrawValid() throws Exception {
        int acc = bankService.createAccount("Eve", "p", AccountTypeEnum.SAVING);
        Account account = AccountRepo.getInstance().findByAccountNumber(acc);
        bankService.deposit(account, 200.0);
        bankService.withdraw(account, 50.0);
        assertEquals(150.0, account.getBalance(), 0.0001);
    }

    @Test
    @DisplayName("withdraw rejects non-positive amount")
    void withdrawInvalidAmount() throws Exception {
        int acc = bankService.createAccount("Frank", "p", AccountTypeEnum.SAVING);
        Account account = AccountRepo.getInstance().findByAccountNumber(acc);
        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, 0));
        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, -5));
    }

    @Test
    @DisplayName("withdraw fails when insufficient balance")
    void withdrawInsufficient() throws Exception {
        int acc = bankService.createAccount("Grace", "p", AccountTypeEnum.SAVING);
        Account account = AccountRepo.getInstance().findByAccountNumber(acc);
        bankService.deposit(account, 30.0);
        assertThrows(InsufficientBalanceException.class, () -> bankService.withdraw(account, 100.0));
    }
}
