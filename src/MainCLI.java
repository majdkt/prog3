import cliagain.Command;
import cliagain.Menu;
import domainLogic.Manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainCLI {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Menu menu = new Menu(manager);
        menu.run();
    }
}
