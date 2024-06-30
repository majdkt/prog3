package serverClient;

import domainLogic.Manager;
import all.JosCommands;

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
                    case "save":
                        saveState(writer);
                        break;
                    case "load":
                        loadState(writer);
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
            // Client disconnected
            System.out.println("Client disconnected.");
        } catch (NoSuchElementException e) {
            // Client disconnected unexpectedly
            System.out.println("Client disconnected unexpectedly.");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createMedia(PrintWriter writer) {
        manager.create();
        writer.println("AudioFile saved.");
    }

    private void readMedia(PrintWriter writer) {
        manager.read().forEach(writer::println);
    }

    private void updateMedia(Scanner scanner, PrintWriter writer) {
        writer.println("Enter address number to update (e.g., '1' for 'address_1'):");
        String updateAddress = "address_" + scanner.nextLine();
        writer.println("Enter new access count:");
        long newAccessCount = Long.parseLong(scanner.nextLine());
        manager.update(updateAddress, newAccessCount);
        writer.println("Updated successfully.");
    }

    private void deleteMedia(Scanner scanner, PrintWriter writer) {
        writer.println("Enter address number to delete (e.g., '1' for 'address_1'):");
        String deleteAddress = "address_" + scanner.nextLine();
        manager.delete(deleteAddress);
        writer.println("Deleted successfully.");
    }

    private void saveState(PrintWriter writer) {
        try {
            josCommands.saveState(manager);
            writer.println("State saved and can be loaded.");
        } catch (IOException e) {
            writer.println("Failed to save state: " + e.getMessage());
        }
    }

    private void loadState(PrintWriter writer) {
        try {
            manager = josCommands.loadState();
            writer.println("State loaded.");
        } catch (IOException | ClassNotFoundException e) {
            writer.println("Failed to load state: " + e.getMessage());
        }
    }

    private void logout(PrintWriter writer) {
        manager.logout();
        writer.println("Logged out.");
    }
}
