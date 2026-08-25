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

@DisplayName("BankService unit tests: CurrentAccount login, deposit, withdraw (overdraft)")
public class BankServiceCurrentAccountTest {

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
    @DisplayName("login succeeds for current account with correct credentials")
    void loginCurrentSuccess() throws Exception {
        int acc = bankService.createAccount("Carl", "pwd", AccountTypeEnum.CURRENT);
        Account account = bankService.login(acc, "pwd");
        assertNotNull(account);
        assertEquals("Carl", account.getHolderName());
    }

    @Test
    @DisplayName("deposit increases balance for current account")
    void depositCurrentValid() throws Exception {
        int acc = bankService.createAccount("Dana", "p", AccountTypeEnum.CURRENT);
        Account account = AccountRepo.getInstance().findByAccountNumber(acc);
        bankService.deposit(account, 500.0);
        assertEquals(500.0, account.getBalance(), 0.0001);
    }

    @Test
    @DisplayName("deposit rejects non-positive amount for current account")
    void depositCurrentInvalidAmount() throws Exception {
        int acc = bankService.createAccount("Eli", "p", AccountTypeEnum.CURRENT);
        Account account = AccountRepo.getInstance().findByAccountNumber(acc);
        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, 0));
        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, -20));
    }

    @Test
    @DisplayName("withdraw allows overdraft up to limit for current account")
    void withdrawCurrentWithinOverdraft() throws Exception {
        int acc = bankService.createAccount("Fay", "p", AccountTypeEnum.CURRENT);
        Account account = AccountRepo.getInstance().findByAccountNumber(acc);
        // Starting balance 0, overdraft allowed up to -2000
        bankService.withdraw(account, 1500.0);
        assertEquals(-1500.0, account.getBalance(), 0.0001);

        // At exact overdraft limit
        bankService.withdraw(account, 500.0); // balance becomes -2000
        assertEquals(-2000.0, account.getBalance(), 0.0001);
    }

    @Test
    @DisplayName("withdraw rejects when exceeding overdraft limit for current account")
    void withdrawCurrentExceedsOverdraft() throws Exception {
        int acc = bankService.createAccount("Gus", "p", AccountTypeEnum.CURRENT);
        Account account = AccountRepo.getInstance().findByAccountNumber(acc);
        // Attempt to withdraw more than overdraft limit
        assertThrows(InsufficientBalanceException.class, () -> bankService.withdraw(account, 2001.0));
    }

    @Test
    @DisplayName("withdraw rejects non-positive amount for current account")
    void withdrawCurrentInvalidAmount() throws Exception {
        int acc = bankService.createAccount("Hana", "p", AccountTypeEnum.CURRENT);
        Account account = AccountRepo.getInstance().findByAccountNumber(acc);
        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, 0));
        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, -1));
    }
}
