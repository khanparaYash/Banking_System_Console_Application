package util;

public class Color {
    public static final String RESET      = "\u001B[0m";

    // Basic Text Colors
    public static final String CYAN       = "\u001B[36m";
    public static final String WHITE      = "\u001B[37m";

    // High-Intensity (Bright) Text Colors
    public static final String BRIGHT_RED    = "\u001B[91m";
    public static final String BRIGHT_GREEN  = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE   = "\u001B[94m";

    // Background Colors
    public static final String BG_RED     = "\u001B[41m";


    // Text Modifiers
    public static final String BOLD       = "\u001B[1m";

    public static void colorPrint(String text, String colorConstant) {
        IO.println(colorConstant + text + RESET);
    }
}
