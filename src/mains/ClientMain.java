package mains;

import cliPack.Menu;
import domainLogic.Manager;
import serverClient.ServerLogic;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {
    public static void main(String[] args) {
        Thread clientThread = new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 9000); // Connect to localhost on port 9000
                System.out.println("Connected to server");
                MainCLI.main(new String[0]);
                socket.close(); // Close the socket when done
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }
}
