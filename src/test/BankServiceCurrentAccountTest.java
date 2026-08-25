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
    @DisplayName("create current account and login with correct password")
    void createAccountAndLogin_success() throws Exception {
        int accNo = bankService.createAccount("Carl", "pwd", AccountTypeEnum.CURRENT);
        Account account = bankService.login(accNo, "pwd");

        assertNotNull(account);
        assertEquals("Carl", account.getHolderName());
        assertEquals("Current", account.getAccountType());
        assertEquals(0.0, account.getBalance(), 0.0001);
    }

    @Test
    @DisplayName("login fails with wrong password for current account")
    void login_wrongPassword_throws() throws Exception {
        int accNo = bankService.createAccount("Carl", "pwd", AccountTypeEnum.CURRENT);

        assertThrows(AuthenticationException.class, () -> bankService.login(accNo, "wrong"));
    }

    @Test
    @DisplayName("login fails for non-existent current account")
    void login_accountNotFound_throws() {
        assertThrows(AccountNotFoundException.class, () -> bankService.login(999999, "pwd"));
    }

    @Test
    @DisplayName("deposit valid amount for current account")
    void deposit_validAmount_increasesBalance() throws Exception {
        int accNo = bankService.createAccount("Dana", "p", AccountTypeEnum.CURRENT);
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        bankService.deposit(account, 500.0);

        assertEquals(500.0, account.getBalance(), 0.0001);
    }

    @Test
    @DisplayName("deposit rejects zero and negative amount for current account")
    void deposit_invalidAmount_throws() throws Exception {
        int accNo = bankService.createAccount("Eli", "p", AccountTypeEnum.CURRENT);
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, 0));
        assertThrows(InvalidAmountException.class, () -> bankService.deposit(account, -20));
    }

    @Test
    @DisplayName("withdraw within overdraft limit succeeds for current account")
    void withdraw_withinOverdraft_success() throws Exception {
        int accNo = bankService.createAccount("Fay", "p", AccountTypeEnum.CURRENT);
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        bankService.withdraw(account, 1500.0);
        assertEquals(-1500.0, account.getBalance(), 0.0001);

        bankService.withdraw(account, 500.0);
        assertEquals(-2000.0, account.getBalance(), 0.0001);
    }

    @Test
    @DisplayName("withdraw at exact overdraft limit is allowed")
    void withdraw_exactOverdraftLimit_success() throws Exception {
        int accNo = bankService.createAccount("Gus", "p", AccountTypeEnum.CURRENT);
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        bankService.withdraw(account, 2000.0);
        assertEquals(-2000.0, account.getBalance(), 0.0001);
    }

    @Test
    @DisplayName("withdraw beyond overdraft limit fails for current account")
    void withdraw_exceedsOverdraft_throws() throws Exception {
        int accNo = bankService.createAccount("Hana", "p", AccountTypeEnum.CURRENT);
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        assertThrows(InsufficientBalanceException.class, () -> bankService.withdraw(account, 2001.0));
    }

    @Test
    @DisplayName("withdraw rejects zero and negative amount for current account")
    void withdraw_invalidAmount_throws() throws Exception {
        int accNo = bankService.createAccount("Iris", "p", AccountTypeEnum.CURRENT);
        Account account = AccountRepo.getInstance().findByAccountNumber(accNo);

        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, 0));
        assertThrows(InvalidAmountException.class, () -> bankService.withdraw(account, -1));
    }
}
