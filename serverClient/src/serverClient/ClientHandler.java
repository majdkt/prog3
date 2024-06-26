package serverClient;

import cliPack.Menu;
import domainLogic.Manager;

import java.io.IOException;
import java.net.Socket;


class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ServerLogic serverLogic;

    public ClientHandler(Socket clientSocket, ServerLogic serverLogic) throws IOException {
        this.clientSocket = clientSocket;
        this.serverLogic = serverLogic;
    }

    @Override
    public void run() {
    }
}
