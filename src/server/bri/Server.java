package server.bri;

import server.bri.services.AmaService;
import server.bri.services.ProgService;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements Runnable {
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
        while(true) {
            Service service = null;

            switch(port) {
                case 4000:
                    service = new ProgService();
                    break;
                default:
                    service = new AmaService();
            }
            new Thread(service).start();
        }
    }
}
