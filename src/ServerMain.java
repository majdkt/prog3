import domainLogic.Manager;
import serverClient.ServerLogic;
import serverClient.ServerManager;

public class ServerMain {
    public static void main(String[] args) {
        ServerLogic serverLogic = new ServerLogic(9000);
        serverLogic.start();
    }
}
