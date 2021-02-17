package server.bri.services;


import server.bri.NetworkData;
import server.bri.Service;
import server.bri.ServiceRegistry;

import java.net.Socket;

public class AmaService implements Service {
    private NetworkData net;

    public AmaService(Socket socket) {
        net = new NetworkData(socket);
    }

    @Override
    public void run() {
        try {
            Class.forName("server.bri.ServiceRegistry");
            String services = ServiceRegistry.serviceListing();
            net.send(services);
            net.send("Choose one of them, please ?");

            int serviceToLaunch = Integer.parseInt(net.read().toString());

            Class<?> serviceClass = ServiceRegistry.getService(serviceToLaunch);

            // instanciate
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // list all services

    }
}
