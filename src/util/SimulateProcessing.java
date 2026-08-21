package util;

import static java.lang.IO.*;


public class SimulateProcessing {
    public static void AddWaiting(String message) {
        print(message);
        try {
            for (int i = 0; i < 4; i++) {
                print(". ");
                Thread.sleep(600); // Pauses for 0.6 seconds between each dot
            }
            println("\n"); // Moves to a new line
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
