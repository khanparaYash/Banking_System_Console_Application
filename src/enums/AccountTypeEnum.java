package enums;

public enum AccountTypeEnum {
    SAVING(1),
    CURRENT(2);

    private final int value;

    AccountTypeEnum(int value) {
        this.value = value;
    }

    public static AccountTypeEnum fromCode(int code) {
        for (AccountTypeEnum op : values()) {
            if (op.value == code) {
                return op;
            }
        }
        throw new IllegalArgumentException("Invalid code");
    }
}
