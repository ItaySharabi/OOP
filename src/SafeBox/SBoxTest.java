package SafeBox;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SBoxTest {
    static safeBox safe;
    static String key;
    static Runnable person;

    @BeforeAll
    static void init() {
        safe = new SBox("123");
        key = "123";
    }

    @Test
    void testOpenLock() {
        boolean b = safe.open("key");
        b |= safe.open(key);
        assertTrue(b);
        b = safe.lock();
        // Safe needs to be open after one call for 60 seconds. 20 for now.
        b &= (safe.open(key) && safe.open(key)) && (safe.open(key) || safe.open("333"));
        assertTrue(b);
        double sum = 100;
        // Able to perform actions while the safe is open.
        assertTrue(safe.deposit(100) == sum);
        safe.lock(); // Unable to deposit after locking.
        assertTrue(safe.deposit(1) == -1);

    }
// Does not run threads... Interesting.
//    @Test
//    void testSomething() {
//        System.out.print("Main opens the safe " + safe.open(key) + "\n");
//        init_person(0);
//        doActions();
//        safe.deposit(100);
//    }
//
//    public static void doActions() {
//
//        Thread actions = new Thread (person);
//        actions.start();
//    }
//
//    private static void init_person(int i) {
//        if (person != null) {
//            System.out.println("Creating person " + i + ":");
//            person = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        System.out.println("- Hello");
//                        Thread.sleep(1000);
//                        System.out.println("- Whats the password... ? ");
//                        Thread.sleep(2000);
//                        System.out.println("- Ohh ok");
//                        Thread.sleep(2000);
//                        if (safe.open(key)) {
//                            System.out.println("Person " + i + " opened the safe");
//                            Thread.sleep(2000);
//                            System.out.println("- I'll take $10 out of the safe");
//                            System.out.println("Balance: " + safe.withdraw(10));
//                            System.out.println("- Maybe i need like $40 more");
//                            safe.withdraw(40);
//                            Thread.sleep(5000);
//                            System.out.println("- Now there's ... " + safe.balance() + " in the safe");
//                            safe.lock();
//                            System.out.println("- Ok Person (" + i + ") is Done");
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//        } else {
//            System.out.println("Person p is null\nCreating some random runnable:");
//            person = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(3000);
//                        System.out.println("- Hello, i'll just open and close the safe.");
//                        if(safe.open(key)) {
//                            Thread.sleep(2000);
//                            System.out.println("- Now i'll close it");
//                            safe.lock();
//                        } else {
//                            System.out.println("WTF");
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//        }
//    }

}