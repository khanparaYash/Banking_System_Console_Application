package util;

public class AccountNumberGenerator {
    private static int counter = 99999;

    public static int generate() {
        return  (++counter);
    }
}
