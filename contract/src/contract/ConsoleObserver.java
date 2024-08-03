package contract;

public class ConsoleObserver implements Observer {
    @Override
    public void update(String message) {
        System.out.println("Observer: " + message);
    }
}