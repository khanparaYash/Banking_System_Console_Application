package enums;

public enum MainMenu {
    CREATE_ACCOUNT(1),
    SECURE_LOGIN(2),
    ADMIN(3),
    EXIT_TERMINAL(4);

    private final int code;
    MainMenu(int code) {
        this.code = code;
    }

    public static MainMenu fromCode(int code) {
        for (MainMenu op : values()) {
            if (op.code == code) {
                return op;
            }
        }
        throw new IllegalArgumentException("Invalid code");
    }

}
