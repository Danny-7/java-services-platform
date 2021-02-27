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
            try {
                if (port == 4000) {
                    service = new ProgService(sSocket.accept());
                }
                else if(port == 3000) {
                    service = new AmaService(sSocket.accept());
                }
            }catch(IOException e) {
                e.printStackTrace();
            }

            new Thread(service).start();
        }
    }
}
