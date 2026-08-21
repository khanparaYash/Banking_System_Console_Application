import model.Account;
import service.BankService;
import util.Color;
import enums.*;

import static java.lang.IO.*;

int readIntInput(String prompt) {
    while (true) {
        try {
            return Integer.parseInt(IO.readln(Color.WHITE + prompt + Color.RESET));
        } catch (Exception e) {
            Color.colorPrint(" ERROR  Invalid entry! Please enter a valid number.", Color.BG_RED + Color.BRIGHT_BLUE + Color.BOLD);
        }
    }
}

double readDoubleInput(String prompt) {
    while (true) {
        try {
            return Double.parseDouble(IO.readln(Color.WHITE + prompt + Color.RESET));
        } catch (Exception e) {
            Color.colorPrint(" ERROR  Invalid decimal amount!", Color.BG_RED + Color.BRIGHT_BLUE + Color.BOLD);
        }
    }
}

// Displays the account operations menu after successful login
void accountMenu(Account account, BankService bankService) {
    while (true) {
        try {
            // Print account menu options
            Color.colorPrint("  \n===== ACCOUNT MENU =====   ", Color.BRIGHT_BLUE + Color.BOLD);
            Color.colorPrint(" 1.  Deposit", Color.CYAN);
            Color.colorPrint(" 2.  Withdraw", Color.CYAN);
            Color.colorPrint(" 3.  Check Balance", Color.CYAN);
            Color.colorPrint(" 4.  View History", Color.CYAN);
            Color.colorPrint(" 5.  Change Password ", Color.CYAN);
            Color.colorPrint(" 6.  Transfer", Color.CYAN);
            Color.colorPrint(" 7.  Logout", Color.CYAN);

            // Read user's menu choice
            int choice = readIntInput(" Enter your choice: ");
            AccountMenu menu = AccountMenu.fromCode(choice);
            switch (menu) {
                case DEPOSIT:
                    // Deposit money into account
                    double amount = readDoubleInput("Enter Amount to Deposit: ");
                    bankService.deposit(account, amount);
                    break;
                case WITHDRAW:
                    // Withdraw money from account
                    double withdrawAmount = readDoubleInput("Enter Amount to Withdraw: ");
                    bankService.withdraw(account, withdrawAmount);
                    break;
                case CHECK_BALANCE:
                    // Display current account balance
                    double balance = bankService.showBalance(account);
                    Color.colorPrint("═════════════════════════════════════════", Color.CYAN);
                    Color.colorPrint("Available Balance: $" + balance, Color.BRIGHT_GREEN + Color.BOLD);
                    Color.colorPrint("═════════════════════════════════════════", Color.CYAN);
                    break;
                case VIEW_HISTORY:
                    // Show transaction history
                    bankService.showHistory(account);
                    break;
                case CHANGE_PASSWORD:
                    // Change account password
                    String newPassword = IO.readln(Color.WHITE + " Enter New Password: " + Color.RESET);
                    bankService.ChangePassword(account, newPassword);
                    break;
                case TRANSFER_MONEY:
                    int recipientAccountNumber = readIntInput("Enter recipient account number: ");
                    double transferAmount = readDoubleInput("Enter Amount to Transfer: ");
                    bankService.transfer(account, recipientAccountNumber, transferAmount);
                    break;
                case LOGOUT:
                    // Logout from current account
                    return;
                default:
                    // Invalid menu selection
                    Color.colorPrint(" ERROR  Invalid entry! Please enter a valid number.", Color.BG_RED + Color.BRIGHT_BLUE + Color.BOLD);
            }
        } catch (Exception e) {
            // Handle all runtime exceptions
            Color.colorPrint(" REJECTED  " + e.getMessage(), Color.BG_RED + Color.BRIGHT_BLUE + Color.BOLD);
        }
    }
}

void adminMenu(BankService bankService) {
    while (true) {
        Color.colorPrint("\n===== ADMIN DASHBOARD =====", Color.BRIGHT_BLUE + Color.BOLD);
        Color.colorPrint(" 1. View total bank holdings", Color.CYAN);
        Color.colorPrint(" 2. Top 3 richest account holders", Color.CYAN);
        Color.colorPrint(" 3. Top 5 balances", Color.CYAN);
        Color.colorPrint(" 4. Account count by type", Color.CYAN);
        Color.colorPrint(" 5. Logout", Color.CYAN);

        int choice = readIntInput(" Enter your choice: ");
        AdminMenu menu = AdminMenu.fromCode(choice);
        try {
            switch (menu) {
                case total_holdings -> {
                    double totalHoldings = bankService.getTotalBankHoldings();
                    Color.colorPrint("Total Bank Holdings: $" + totalHoldings, Color.BRIGHT_GREEN + Color.BOLD);
                }
                case Top_3_richest_account -> {
                    List<String> richHolders = bankService.getTop3RichestAccountHolders();
                    Color.colorPrint("Top 3 Richest Account Holders:", Color.BRIGHT_YELLOW + Color.BOLD);
                    for (int index = 0; index < richHolders.size(); index++) {
                        Color.colorPrint((index + 1) + ". " + richHolders.get(index), Color.CYAN);
                    }
                }
                case Top_5_balances -> {
                    List<Double> balances = bankService.getTop5Balances();
                    Color.colorPrint("Top 5 Balances:", Color.BRIGHT_YELLOW + Color.BOLD);
                    for (int index = 0; index < balances.size(); index++) {
                        Color.colorPrint((index + 1) + ". $" + balances.get(index), Color.CYAN);
                    }
                }
                case Account_count_by_type -> {
                    Map<String, Long> counts = bankService.countAccountsByType();
                    Color.colorPrint("Account Count by Type:", Color.BRIGHT_YELLOW + Color.BOLD);
                    for (Map.Entry<String, Long> entry : counts.entrySet()) {
                        Color.colorPrint(entry.getKey() + ": " + entry.getValue(), Color.CYAN);
                    }
                }
                case Logout -> {
                    return;
                }
                default ->
                        Color.colorPrint(" ERROR  Invalid entry! Please enter a valid number.", Color.BG_RED + Color.BRIGHT_BLUE + Color.BOLD);
            }
        } catch (Exception e) {
            Color.colorPrint(" REJECTED  " + e.getMessage(), Color.BG_RED + Color.BRIGHT_BLUE + Color.BOLD);
        }
    }
}

void main() {
    // Service layer object
    BankService bankService = BankService.getInstance();
    while (true) {
        // Main banking portal menu
        Color.colorPrint("\n=== BANKING PORTAL ===  ", Color.BRIGHT_BLUE + Color.BOLD);
        Color.colorPrint(" 1. Create Account", Color.CYAN);
        Color.colorPrint(" 2. Secure Login", Color.CYAN);
        Color.colorPrint(" 3. Admin Login", Color.CYAN);
        Color.colorPrint(" 4. Exit Terminal", Color.CYAN);
        try {
            // Read user choice
            int choice = readIntInput(" Enter your choice: ");
            MainMenu menu = MainMenu.fromCode(choice);
            switch (menu) {

                case CREATE_ACCOUNT:
                    // Create a new bank account
                    Color.colorPrint("\n1. Saving Account", Color.CYAN);
                    Color.colorPrint("2. Current Account", Color.CYAN);
                    int type = readIntInput(" Enter your choice: ");
                    AccountTypeEnum accountType = AccountTypeEnum.fromCode(type);

                    if (accountType == AccountTypeEnum.SAVING || accountType == AccountTypeEnum.CURRENT) {
                        // Read account details
                        String holderName = IO.readln(Color.WHITE + "\nEnter Account Holder Name: " + Color.RESET);
                        String password = IO.readln(Color.WHITE + "Create Account Password: " + Color.RESET);
                        // Create account and display generated account number
                        Color.colorPrint("Generated Account Number: " + bankService.createAccount(holderName, password, accountType), Color.BRIGHT_YELLOW + Color.BOLD);
                        Color.colorPrint("Successfully created", Color.BRIGHT_GREEN + Color.BOLD);
                    } else {
                        // Invalid account type
                        Color.colorPrint(" ERROR  Invalid entry! Please enter a valid number.", Color.BG_RED + Color.BRIGHT_BLUE + Color.BOLD);
                    }
                    break;

                case SECURE_LOGIN:
                    // Login to existing account
                    int accountNumber = readIntInput("Enter account number :");
                    String pw = IO.readln(Color.WHITE + "Enter password : " + Color.RESET);
                    // Authenticate user
                    Account account = bankService.login(accountNumber, pw);
                    Color.colorPrint("LogIn successfully Done! .", Color.BRIGHT_GREEN);
                    // Open account menu after successful login
                    accountMenu(account, bankService);
                    break;
                case ADMIN:
                    String adminUser = IO.readln(Color.WHITE + "Enter admin username : " + Color.RESET);
                    String adminPassword = IO.readln(Color.WHITE + "Enter admin password : " + Color.RESET);
                    if ("admin".equals(adminUser) && "admin123".equals(adminPassword)) {
                        Color.colorPrint("Admin login successful.", Color.BRIGHT_GREEN);
                        adminMenu(bankService);
                    } else {
                        Color.colorPrint(" ERROR  Invalid admin credentials.", Color.BG_RED + Color.BRIGHT_BLUE + Color.BOLD);
                    }
                    break;
                case EXIT_TERMINAL:
                    // Exit application
                    println("Thank You, Visit Again");
                    System.exit(0);
                    break;
                default:
                    // Invalid option selected
                    Color.colorPrint(" ERROR  Invalid entry! Please enter a valid number.", Color.BG_RED + Color.BRIGHT_BLUE + Color.BOLD);
            }
        } catch (Exception e) {
            // Global exception handling
            Color.colorPrint(" REJECTED  " + e.getMessage(), Color.BG_RED + Color.BRIGHT_BLUE + Color.BOLD);
        }

    }
}
