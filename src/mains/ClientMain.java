package mains;

import all.ClientMenuHandler;
import all.ServerInterface;
import cliPack.Menu;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) {
        try {
            ServerInterface serverInterface = new ServerInterface();
            ClientMenuHandler clientHandler = new ClientMenuHandler(serverInterface);
            Menu menu = new Menu(clientHandler);
            menu.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
