package serverClient;

import domainLogic.Manager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPClientHandler implements Runnable {
    private final DatagramPacket packet;
    private final Manager manager;

    public UDPClientHandler(DatagramPacket packet, Manager manager) {
        this.packet = packet;
        this.manager = manager;
    }

    @Override
    public void run() {
        try {
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received from client: " + message);
            // Process the message and respond
            String response = "Server received: " + message;
            DatagramPacket responsePacket = new DatagramPacket(response.getBytes(), response.length(),
                    packet.getAddress(), packet.getPort());
            DatagramSocket socket = new DatagramSocket();
            socket.send(responsePacket);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
