# Banking System Console Application

A Java-based console banking system for creating accountFactories, performing transactions, checking balances, viewing history, transferring funds, and managing an admin dashboard.

## Project Overview

This project simulates a basic banking portal with:
- Savings and current account support
- Secure customer login
- Deposit, withdrawal, transfer, and balance viewing
- Transaction history tracking
- Password change support
- Admin analytics for bank-wide summaries

## Features

- Create a new bank account with either:
  - Saving Account
  - Current Account
- Login using account number and password
- Deposit money into the account
- Withdraw money with validation
- Check current account balance
- View transaction history
- Change account password
- Transfer money to another account
- Admin login with dashboard features:
  - Total bank holdings
  - Top 3 richest account holders
  - Top 5 balances
  - Account count by type
- Generate unique account numbers automatically
- Colored console interface for better readability

## Tech Stack

- Java SE
- Object-Oriented Programming (OOP)
- Console-based UI
- Collections and Streams API

## Project Structure

```text
PROJECT_1/
├── src/
│   ├── BankingApp.java
│   ├── enums/
│   │   ├── AccountMenu.java
│   │   ├── AdminMenu.java
│   │   └── MainMenu.java
│   ├── exception/
│   │   ├── AccountNotFoundException.java
│   │   ├── AuthenticationException.java
│   │   ├── InsufficientBalanceException.java
│   │   └── InvalidAmountException.java
│   ├── model/
│   │   ├── Account.java
│   │   ├── CurrentAccount.java
│   │   ├── SavingAccount.java
│   │   └── Transaction.java
│   ├── repo/
│   │   └── AccountRepo.java
│   ├── service/
│   │   └── BankService.java
│   └── util/
│       ├── AccountNumberGenerator.java
│       ├── Color.java
│       └── SimulateProcessing.java
├── out/
├── .idea/
├── PROJECT 1 – BANKING SYSTEM CONSOLE APPLICATION.docx
└── README.md
```

## How to Run

### Option 1: From the terminal

1. Open a terminal in the project root.
2. Compile the Java source files.

For macOS/Linux:

```bash
javac -d out $(find src -name "*.java")
```

For Windows PowerShell:

```powershell
Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object { $_.FullName } | ForEach-Object { javac -d out $_ }
```

3. Run the application:

```bash
java -cp out BankingApp
```

If your Java compiler keeps package names in the output directory, use:

```bash
java -cp out model.BankingApp
```

If the project is opened in an IDE such as IntelliJ IDEA or VS Code, you can run `BankingApp` directly from the IDE.

### Option 2: IntelliJ IDEA

1. Open the project folder in IntelliJ IDEA.
2. Locate `src/BankingApp.java`.
3. Click Run or select the main class `BankingApp`.

## Default Admin Credentials

```text
Username: admin
Password: admin123
```

## Sample Input and Output

### Account creation

```text
=== BANKING PORTAL ===
1. Create Account
2. Secure Login
3. Admin Login
4. Exit Terminal

Enter your choice: 1

1. Saving Account
2. Current Account
Enter your choice: 1
Enter Account Holder Name: Yash
Create Account Password: 1234
Generated Account Number: 100000
Successfully created
```

### Login and deposit

```text
Enter account number : 100000
Enter password : 1234
LogIn successfully Done!

===== ACCOUNT MENU =====
1. Deposit
2. Withdraw
3. Check Balance
4. View History
5. Change Password
6. Transfer
7. Logout

Enter your choice: 1
Enter Amount to Deposit: 5000
Deposit Successfully Done
Amount Added: 5000.0
Updated Balance: $5000.0
```

### Admin dashboard

```text
Enter admin username : admin
Enter admin password : admin123
Admin login successful.

===== ADMIN DASHBOARD =====
1. View total bank holdings
2. Top 3 richest account holders
3. Top 5 balances
4. Account count by type
5. Logout
```

## UML Class Diagram

The standalone version is available in [uml-diagram.md](./uml-diagram.md).

```mermaid
classDiagram
    class BankingApp {
        +main()
    }

    class BankService {
        +createAccount(holderName: String, password: String, type: int): int
        +login(accountNumber: int, password: String): Account
        +deposit(account: Account, amount: double)
        +withdraw(account: Account, amount: double)
        +transfer(sourceAccount: Account, targetAccountNumber: int, amount: double)
        +showBalance(account: Account): double
        +showHistory(account: Account)
        +ChangePassword(account: Account, newPassword: String)
        +getTotalBankHoldings(): double
        +getTop3RichestAccountHolders(): List<String>
        +getTop5Balances(): List<Double>
        +countAccountsByType(): Map<String, Long>
    }

    class AccountRepo {
        -accountFactories: Map<Integer, Account>
        +getAccounts(): List<Account>
        +save(accountNumber: int, account: Account): int
        +findByAccountNumber(accountNumber: int): Account
    }

    class abstract Account {
        -accountNumber: int
        -holderName: String
        -password: String
        -balance: double
        -transections: List<Transaction>
        +getHolderName(): String
        +getPassword(): String
        +setPassword(password: String)
        +getAccountNumber(): int
        +getBalance(): double
        +setBalance(balance: double)
        +getTransections(): List<Transaction>
        +getAccountType(): String
        +deposit(amount: double, showMessage: boolean)
        +withdraw(amount: double, showMessage: boolean)
    }

    class SavingAccount {
        +getAccountType(): String
        +deposit(amount: double, showMessage: boolean)
        +withdraw(amount: double, showMessage: boolean)
    }

    class CurrentAccount {
        +getAccountType(): String
        +deposit(amount: double, showMessage: boolean)
        +withdraw(amount: double, showMessage: boolean)
    }

    class Transaction {
        -type: String
        -amount: double
        -dateTime: LocalDateTime
        -finalBalance: double
        +toString(): String
    }

    class AccountNumberGenerator {
        +generate(): int
    }

    class MainMenu
    class AccountMenu
    class AdminMenu

    class AccountNotFoundException
    class AuthenticationException
    class InvalidAmountException
    class InsufficientBalanceException

    BankingApp --> BankService
    BankService --> AccountRepo
    BankService --> Account
    AccountRepo --> Account
    Account <|-- SavingAccount
    Account <|-- CurrentAccount
    Account --> Transaction
    AccountNumberGenerator ..> Account
    BankingApp ..> MainMenu
    BankingApp ..> AccountMenu
    BankingApp ..> AdminMenu
    Account ..> AccountNotFoundException
    Account ..> AuthenticationException
    Account ..> InvalidAmountException
    Account ..> InsufficientBalanceException
```

## Notes

- The project stores account data in memory while the application is running.
- It is intended as a learning and demonstration console-based banking application.
- It can be extended with file storage, database integration, or a web interface.

