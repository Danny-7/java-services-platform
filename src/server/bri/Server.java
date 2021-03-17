package server.bri;

import server.bri.services.AmaService;
import server.bri.services.ProgService;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements Runnable, AutoCloseable {
    private ServerSocket sSocket;
    private int port;

    public Server(int port) {
        try {
            sSocket = new ServerSocket(port);
            this.port = port;
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            try {
                switch (port) {
                    case 4000 -> new ProgService(sSocket.accept());
                    case 3000 -> new AmaService(sSocket.accept());
                    default -> throw new IOException("This port doesn't exist");
                }
            } catch (IOException e) {
                System.err.println("[Server] " + e.getMessage());
                e.printStackTrace();
                running = false;
            }
        }
    }

    @Override
    public void close() throws Exception {
        if(!sSocket.isClosed())
            sSocket.close();
    }
}
