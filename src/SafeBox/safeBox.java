package SafeBox;

public interface safeBox {

    boolean open(String key);
    boolean lock();
    double balance();
    double deposit (double n);
    double withdraw(double n);
    String toString();
}
