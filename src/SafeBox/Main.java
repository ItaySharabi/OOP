package SafeBox;

public class Main {

    static safeBox safe;
    private static String key = "12341234";
    private static Runnable person;

    public static void main(String[] args) {
        safe = new SBox();


        System.out.print("Main opens the safe " + safe.open(key) + "\n");
        // Create a random runnable first
        // (Has nothing to do with i=0).
        init_person(0);
        doActions();
//        safe.deposit(100);




    }

    public static void doActions() {

        Thread actions = new Thread (person);
        actions.start();
    }

    private static void init_person(int i) {
        if (person != null) {
            System.out.println("Creating person " + i + ":");
            person = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("- Hello");
                        Thread.sleep(1000);
                        System.out.println("- Whats the password... ? ");
                        Thread.sleep(2000);
                        System.out.println("- Ohh ok");
                        Thread.sleep(2000);
                        if (safe.open(key)) {
                            System.out.println("Person " + i + " opened the safe");
                            Thread.sleep(2000);
                            System.out.println("- I'll take $10 out of the safe");
                            System.out.println("Balance: " + safe.withdraw(10));
                            System.out.println("- Maybe i need like $40 more");
                            safe.withdraw(40);
                            Thread.sleep(5000);
                            System.out.println("- Now there's ... " + safe.balance() + " in the safe");
                            System.out.println("- Now i'll lock the safe...");
                            safe.lock();
                            System.out.println("- Ok Person (" + i + ") is Done");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        } else {
            System.out.println("Person p is null\nCreating some random runnable:");
            person = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        System.out.println("- (Random) Hello, i'll just open and close the safe.");
                        if(safe.open(key)) {
                            Thread.sleep(2000);
                            System.out.println("- (Random) Now i'll close it");
                            safe.lock();
                        } else {
                            System.out.println("(Random) WTF Someone closed the safe!");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
    }
}
