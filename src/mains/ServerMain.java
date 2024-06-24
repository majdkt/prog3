package mains;

import domainLogic.Manager;
import all.JosCommands;
import all.ServerMenuHandler;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        Manager manager = new Manager();
        JosCommands josCommands = new JosCommands();
        ServerMenuHandler serverHandler = new ServerMenuHandler(manager, josCommands);

        ExecutorService executor = Executors.newFixedThreadPool(10); // For handling multiple clients

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                executor.submit(() -> handleClient(clientSocket, serverHandler));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static void handleClient(Socket socket, ServerMenuHandler handler) {
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

            Object command;
            while ((command = ois.readObject()) != null) {
                String response = handler.handleCommand(command);
                oos.writeObject(response);
                oos.flush();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
