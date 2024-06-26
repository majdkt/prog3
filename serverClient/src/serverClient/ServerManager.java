package serverClient;

import domainLogic.Manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManager {
    private int port;
    ServerLogic serverLogic;

    public ServerManager(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("Server started on port " + this.port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());
                // Handle client connection using ClientHandler
                ClientHandler clientHandler = new ClientHandler(socket, serverLogic);
                clientHandler.run();
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
