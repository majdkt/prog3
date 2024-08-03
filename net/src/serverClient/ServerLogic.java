package serverClient;

import domainLogic.Manager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerLogic {
    private final int port;
    private final String protocol; // "TCP" or "UDP"
    private boolean running;
    private ServerSocket serverSocket;
    private DatagramSocket datagramSocket;
    private Manager manager;

    public ServerLogic(int port, String protocol, Manager manager) {
        this.port = port;
        this.protocol = protocol;
        this.manager = manager;
    }

    public void start() {
        try {
            if ("TCP".equalsIgnoreCase(protocol)) {
                serverSocket = new ServerSocket(port);
                System.out.println("TCP Server started on port " + port);
                running = true;

                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("TCP Client connected: " + clientSocket.getInetAddress());
                    ClientHandler clientHandler = new ClientHandler(clientSocket, manager);
                    new Thread(clientHandler).start();
                }
            } else if ("UDP".equalsIgnoreCase(protocol)) {
                datagramSocket = new DatagramSocket(port);
                System.out.println("UDP Server started on port " + port);
                running = true;

                byte[] buffer = new byte[65535];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                while (running) {
                    datagramSocket.receive(packet);
                    System.out.println("UDP Client connected: " + packet.getAddress());
                    UDPClientHandler udpClientHandler = new UDPClientHandler(packet, manager);
                    new Thread(udpClientHandler).start();
                }
            } else {
                System.out.println("Unsupported protocol: " + protocol);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (datagramSocket != null && !datagramSocket.isClosed()) {
                datagramSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
