package SafeBox;

public class SBox implements safeBox {
    private String password;
    private double balance;
    private boolean isOpen;
    private static int autoCloseTime;

    public SBox() {
        this("12341234"); // Default password.
    }

    public SBox(String password) {
        this.password = password;
        this.balance = 0;
        autoCloseTime = 20 * 1000; // 20 Seconds.
        isOpen = false;
    }

    /**
     * Calling this method with the right key
     * will open the safe for 'this.autoCloseTime' seconds.
     * This method issues a thread that sleeps for the specified time
     * and closes the safe.
     * After 30 seconds the thread checks if the safe is closed and
     * if it is it stops with Thread.currentThread.stop(). TODO: Might not be good
     * @param key - the password to the safe.
     * @return true if the safe was already open or was opened after call
     *         false if otherwise failed to open somehow.
     */
    @Override
    public boolean open(String key) {
        // If the safe is locked and the password is correct.
        if (!isOpen && password.compareTo(key) == 0) {
            isOpen = true;

            // Create a new runnable which sleeps for a minute
            // and locks the safe.
            Runnable waitAndLock = () -> { // Using lambda for the first time...
                synchronized (this) { // This will make a queue of the threads trying to access
                    // This particular object.
                    try {
                        if (isOpen) { // Recheck if the safe is opened.
                            System.out.println("Safe is now open");
                            System.out.println(autoCloseTime/1000 + " seconds remaining");
                            Thread.sleep(autoCloseTime / 2);

                            if (!isOpen) { // If thread came back from sleeping and safe is closed.
                                System.err.println("Timer stopped!");
                                Thread.currentThread().stop();
                            }

                            System.out.println(autoCloseTime/2000 + " seconds remaining");
                            Thread.sleep(autoCloseTime / 2);

                            if (!isOpen) { // If thread came back from sleeping and safe is closed.
                                System.err.println("Timer stopped!");
                                Thread.currentThread().stop();
                            }
                            System.out.println("Automatically locking safe");
                            lock();
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Sleeping interrupted");
                    }
                }

            };

            // Set up a thread with the timer and run it.
            Thread t = new Thread(waitAndLock);
            t.start();

            return true;
        } else if (isOpen) {
            return true;
        }
        System.out.println("Incorrect password!");
        return false;
    }

    @Override
    public boolean lock() {
        if (isOpen) {
            isOpen = false;
            System.out.println("Safe Manually locked");
            return true;
        } else {
            System.out.println("Safe is already locked");
            return false;
        }
    }

    @Override
    public double balance() {
        if (isOpen) {
            System.out.println("Balance: $" + balance);
            return this.balance;
        } else {
            System.err.println("Safe is locked.\n\tOpen to check the balance.");
            return -1;
        }
    }

    @Override
    public double deposit(double n) {
        if (isOpen) {
            System.out.println("Successfully deposited: $" + n + "");
            this.balance += n;
            return balance;
        } else {
            System.err.println("Safe is locked.\n\tOpen to deposit cash.");
            return -1;
        }
    }

    @Override
    public double withdraw(double n) {
        if (isOpen) {
            if (balance - n >= 0) {
                this.balance -= n;
            } else {
                System.out.println("Not enough cash in the safe.");
                System.out.println("Balance is: $" + balance);
            }

            return balance;
        } else {
            System.err.println("Safe is locked.\n\tOpen to withdraw");
            return -1;
        }
    }

    public boolean setPassword(String newPassword) {
        if (isOpen) {
            System.err.println("Close the safe to change the password!");
            return false;
        } else {
            System.out.println("Changed password");
            this.password = newPassword;
            return true;
        }
    }
}
