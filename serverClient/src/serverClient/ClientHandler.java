package serverClient;

import domainLogic.Manager;
import commands.JosCommands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private Manager manager;
    private final JosCommands josCommands = new JosCommands();

    public ClientHandler(Socket clientSocket, Manager manager) {
        this.clientSocket = clientSocket;
        this.manager = manager;
    }

    @Override
    public void run() {
        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream();
             Scanner scanner = new Scanner(input);
             PrintWriter writer = new PrintWriter(output, true)) {

            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                switch (command.toLowerCase()) {
                    case "help":
                        writer.println("create, read, update, delete, logout, exit");
                        break;
                    case "create":
                        createMedia(writer);
                        break;
                    case "read":
                        readMedia(writer);
                        break;
                    case "update":
                        updateMedia(scanner, writer);
                        break;
                    case "delete":
                        deleteMedia(scanner, writer);
                        break;
                    case "logout":
                        logout(writer);
                        break;
                    case "exit":
                        writer.println("Goodbye!");
                        return;  // Exit the run() method and close the socket
                    default:
                        writer.println("Invalid command. Please try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        } catch (NoSuchElementException e) {
            System.out.println("Client disconnected unexpectedly.");
        } finally {
            try {
                clientSocket.close();
                ServerLogic.clientDisconnected();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createMedia(PrintWriter writer) {
      //  manager.create(new);
        System.out.println("AudioFile saved");
        writer.println("Created new media.");
    }

    private void readMedia(PrintWriter writer) {
        System.out.println("MediaList Sent");

     //   List<String> mediaList = manager.read();

    }

    private void updateMedia(Scanner scanner, PrintWriter writer) {
        writer.println("Enter address number to update (e.g., '1' for 'address_1'):");
        String updateAddress = "address_" + scanner.nextLine();
        writer.println("Enter new access count:");
        long newAccessCount = Long.parseLong(scanner.nextLine());
      //  manager.update(updateAddress, newAccessCount);
        System.out.println("Access updated.");
        writer.println("Updated successfully: " + newAccessCount + " access count");
    }

    private void deleteMedia(Scanner scanner, PrintWriter writer) {
        writer.println("Enter address number to delete (e.g., '1' for 'address_1'):");
        String deleteAddress = "address_" + scanner.nextLine();
      //  manager.delete(deleteAddress);
        writer.println("Deleted successfully.");
        System.out.println("Media Deleted.");
    }


    private void logout(PrintWriter writer) {
       // manager.logout();
        System.out.println("Logged out successfully.");
        writer.println("Logged out.");
    }
}
