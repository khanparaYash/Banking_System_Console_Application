package enums;


public enum AccountMenu {
      DEPOSIT(1),WITHDRAW(2),CHECK_BALANCE(3),VIEW_HISTORY(4),CHANGE_PASSWORD(5),TRANSFER_MONEY(6),LOGOUT(7);
      public final int choice;

      AccountMenu(int choice) {
          this.choice=choice;
      }
      public static AccountMenu fromCode(int choice) {
          for(AccountMenu a: AccountMenu.values()){
              if(a.choice==choice) return a;
          }
          throw new IllegalArgumentException("Invalid code");
      }
}
