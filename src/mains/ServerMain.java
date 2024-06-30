package mains;

import domainLogic.Manager;
import serverClient.ServerLogic;

public class ServerMain {
    public static void main(String[] args) {
        Manager manager = new Manager();
        ServerLogic serverLogic = new ServerLogic(8080, manager);
        serverLogic.start();
    }
}
