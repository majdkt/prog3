package mains;

import cliPack.Menu;
import domainLogic.Manager;

public class MainCLI {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Menu menu = new Menu(manager);
        menu.run();
    }
}
