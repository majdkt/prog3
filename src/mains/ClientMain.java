package mains;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        final String serverHost = "localhost";
        final int serverPort = 8080;

        try (Socket socket = new Socket(serverHost, serverPort);
             InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream();
             Scanner scanner = new Scanner(input);
             PrintWriter writer = new PrintWriter(output, true);
             Scanner userInputScanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");

            String userInput;
            while (true) {
                System.out.print("Enter command: ");
                userInput = userInputScanner.nextLine();
                writer.println(userInput);

                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                // Read server response
                while (scanner.hasNextLine()) {
                    String serverResponse = scanner.nextLine();
                    System.out.println(serverResponse);
                }
            }

            System.out.println("Disconnected from the server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
