package serverClient;

import domainLogic.Manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerLogic {
    private ServerLogic serverLogic;
    private int port;

    public ServerLogic(int port) {
        this.serverLogic = serverLogic;
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle client connection using ClientHandler
                ClientHandler client = new ClientHandler(clientSocket, serverLogic);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}