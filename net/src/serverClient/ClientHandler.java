package serverClient;

import domainLogic.Manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Manager manager;

    public ClientHandler(Socket socket, Manager manager) {
        this.clientSocket = socket;
        this.manager = manager;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);
                out.println("Server received: " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
