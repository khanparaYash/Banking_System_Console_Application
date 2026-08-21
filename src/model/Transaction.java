package model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final String type;
    private final double amount;
    private final LocalDateTime dateTime;
    private final double finalBalance;

    public Transaction(String type, double amount,double finalBalance) {
        this.type = type;
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
        this.finalBalance = finalBalance;
    }

    @Override
    public String toString() {
        return String.format(
                "%-12s %-10.2f %-12.2f %-20s\n",
                type,
                amount,
                finalBalance,
                dateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a")));
    }
}
