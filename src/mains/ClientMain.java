package mains;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        String serverAddress = "localhost";  // The server's address
        int serverPort = 8080;  // The server's port

        try (Socket socket = new Socket(serverAddress, serverPort);
             InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream();
             Scanner serverInput = new Scanner(input);
             PrintWriter serverOutput = new PrintWriter(output, true);
             Scanner userInput = new Scanner(System.in)) {

            System.out.println("Connected to the server.");
            System.out.println("create, read, update, delete, logout, exit");

            // Reading server responses continuously
            Thread responseThread = new Thread(() -> {
                while (serverInput.hasNextLine()) {
                    String line = serverInput.nextLine();
                    System.out.println(line);}
            });
            responseThread.start();

            // Sending commands
            while (true) {
                System.out.print("(help)    ");
                String command = userInput.nextLine();
                serverOutput.println(command);
                serverOutput.flush();
                if (command.equalsIgnoreCase("exit")) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
