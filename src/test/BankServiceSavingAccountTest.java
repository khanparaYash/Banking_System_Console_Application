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

    @BeforeEach
    void setUp() throws Exception {
        bankService = BankService.getInstance();
        AccountRepo repo = AccountRepo.getInstance();
        Field accountsField = AccountRepo.class.getDeclaredField("accounts");
        accountsField.setAccessible(true);
        Map<?, ?> accounts = (Map<?, ?>) accountsField.get(repo);
        accounts.clear();
    }

    @Test
    @DisplayName("create saving account and login with correct password")
    void createAccountAndLogin_success() throws Exception {
        int accNo = bankService.createAccount("Alice", "pwd123", AccountTypeEnum.SAVING);
        Account account = bankService.login(accNo, "pwd123");

        assertNotNull(account);
        assertEquals("Alice", account.getHolderName());
        assertEquals("Saving", account.getAccountType());
        assertEquals(0.0, account.getBalance(), 0.0001);
    }

    @Test
    @DisplayName("login fails with wrong password for saving account")
    void login_wrongPassword_throws() throws Exception {
        int accNo = bankService.createAccount("Bob", "secret", AccountTypeEnum.SAVING);

        assertThrows(AuthenticationException.class, () -> bankService.login(accNo, "badpass"));
    }

    @Test
    @DisplayName("login fails for non-existent saving account")
    void login_accountNotFound_throws() {
        assertThrows(AccountNotFoundException.class, () -> bankService.login(123456789, "nopass"));
    }

    @Test
    @DisplayName("deposit valid amount for saving account")
    void deposit_validAmount_increasesBalance() throws Exception {
        int accNo = bankService.createAccount("Carol", "p", AccountTypeEnum.SAVING);
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        bankService.deposit(account, 150.0);

        assertEquals(150.0, account.getBalance(), 0.0001);
    }

    @Test
    @DisplayName("deposit rejects zero and negative amount for saving account")
    void deposit_invalidAmount_throws() throws Exception {
        int accNo = bankService.createAccount("Dan", "p", AccountTypeEnum.SAVING);
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, 0));
        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, -10));
    }

    @Test
    @DisplayName("withdraw succeeds when sufficient balance for saving account")
    void withdraw_validAmount_success() throws Exception {
        int accNo = bankService.createAccount("Eve", "p", AccountTypeEnum.SAVING);
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        bankService.deposit(account, 200.0);
        bankService.withdraw(account, 50.0);

        assertEquals(150.0, account.getBalance(), 0.0001);
    }

    @Test
    @DisplayName("withdraw rejects zero and negative amount for saving account")
    void withdraw_invalidAmount_throws() throws Exception {
        int accNo = bankService.createAccount("Frank", "p", AccountTypeEnum.SAVING);
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, 0));
        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, -5));
    }

    @Test
    @DisplayName("withdraw fails when insufficient balance for saving account")
    void withdraw_insufficientBalance_throws() throws Exception {
        int accNo = bankService.createAccount("Grace", "p", AccountTypeEnum.SAVING);
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        bankService.deposit(account, 30.0);

        assertThrows(InsufficientBalanceException.class, () -> bankService.withdraw(account, 100.0));
    }
}
