package serverClient;


import domainLogic.Manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerLogic {
    private final int port;
    private boolean running;
    private ServerSocket serverSocket;
    private Manager manager;

    public ServerLogic(int port, Manager manager) {
        this.port = port;
        this.manager = manager;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            running = true;

            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle client connection using ClientHandler
                ClientHandler client = new ClientHandler(clientSocket, manager);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void clientDisconnected() {
        System.out.println("Client disconnected.");
    }
}
