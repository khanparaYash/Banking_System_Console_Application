package test;

import enums.AccountTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.BankService;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BankService createAccount validation tests")
public class BankServiceCreateAccountValidationTest {

    private final BankService bankService = BankService.getInstance();

    //this testcase is defined for creating account with empty name.
    @Test
    @DisplayName("createAccount rejects empty holder name")
    void createAccountRejectsEmptyHolder() {
        assertThrows(IllegalArgumentException.class,
                () -> bankService.createAccount("  ", "pwd", AccountTypeEnum.SAVING));
    }


    // this testcase is defined for creating account with empty password.
    @Test
    @DisplayName("createAccount rejects empty password")
    void createAccountRejectsEmptyPassword() {
        assertThrows(IllegalArgumentException.class,
                () -> bankService.createAccount("Alice", "", AccountTypeEnum.SAVING));
    }


    // this testcase is defined for creating account with null holder name.
    @Test
    @DisplayName("createAccount rejects null holder name")
    void createAccountRejectsNullHolder() {
        assertThrows(IllegalArgumentException.class,
                () -> bankService.createAccount(null, "pwd", AccountTypeEnum.SAVING));
    }


    // this testcase is defined for creating account with null password.
    @Test
    @DisplayName("createAccount rejects null password")
    void createAccountRejectsNullPassword() {
        assertThrows(IllegalArgumentException.class,
                () -> bankService.createAccount("Alice", null, AccountTypeEnum.SAVING));
    }


    //  this testcase is defined for creating account with null account type.
    @Test
    @DisplayName("createAccount rejects null account type")
    void createAccountRejectsNullType() {
        assertThrows(IllegalArgumentException.class,
                () -> bankService.createAccount("Alice", "pwd", null));
    }

}
