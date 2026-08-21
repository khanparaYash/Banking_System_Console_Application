package enums;

public enum AdminMenu {
    total_holdings(1),
    Top_3_richest_account(2),
    Top_5_balances(3),
    Account_count_by_type(4),
    Logout(5);
    private final int code;
    AdminMenu(int code){
        this.code = code;
    }
    public static AdminMenu fromCode(int code) {
        for (AdminMenu op : values()) {
            if (op.code == code) {
                return op;
            }
        }
        throw new IllegalArgumentException("Invalid code");
    }

}
