package all;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerInterface {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ServerInterface() throws IOException {
        this.socket = new Socket(HOST, PORT);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
    }

    public String sendCommand(String command) throws IOException, ClassNotFoundException {
        oos.writeObject(command);
        oos.flush();
        return (String) ois.readObject();
    }

    public List<String> sendReadCommand(String command) throws IOException, ClassNotFoundException {
        oos.writeObject(command);
        oos.flush();
        return (List<String>) ois.readObject();
    }

    public void sendObject(Object obj) throws IOException {
        oos.writeObject(obj);
        oos.flush();
    }

    public void close() throws IOException {
        oos.writeObject(null); // Indicate end of communication
        oos.flush(); // Ensure all data is sent before closing
        oos.close();
        ois.close();
        socket.close();
    }
}
