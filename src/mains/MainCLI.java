package mains;

import cliPack.Menu;
import domainLogic.Manager;

public class MainCLI {
    public static void main(String[] args) {
        Manager manager = new Manager();
        String user = "Majd";
        Menu menu = new Menu(manager,user);
        menu.run();
    }
}
