import model.SavingAccount;
import exception.InsufficientBalanceException;

void main() throws InterruptedException {
        SavingAccount acct = new SavingAccount(1, "Alice", "pw");

        int depositers = 10000;
        int withdrawers = 10000;
        Thread[] dts = new Thread[depositers];
        Thread[] wts = new Thread[withdrawers];

        for (int i = 0; i < depositers; i++) {
            dts[i] = new Thread(() -> acct.deposit(10, false));
            dts[i].start();
        }

        for (int i = 0; i < withdrawers; i++) {
            wts[i] = new Thread(() -> {
                synchronized (acct) {
                    try {
                        acct.withdraw(10, false);
                    } catch (InsufficientBalanceException e) {
                        System.err.println("Withdraw failed: " + e.getMessage());
                    }
                }
            });
            wts[i].start();
        }

        for (Thread t : dts) t.join();
        for (Thread t : wts) t.join();

        System.out.println("Final balance: $" + acct.getBalance());
    }

