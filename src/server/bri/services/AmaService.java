package server.bri.services;


import server.bri.NetworkData;
import server.bri.Service;
import server.bri.managers.ServiceManager;

import java.net.Socket;

public class AmaService implements Service {
    private NetworkData net;

    public AmaService(Socket socket) {
        net = new NetworkData(socket);
    }

    @Override
    public void run() {
        try {
            Class.forName("server.bri.managers.ServiceManager");
            String services = ServiceManager.serviceListing();
            net.send(services);
            net.send("Choose one of them, please ?");

            int serviceToLaunch = Integer.parseInt(net.read().toString());

            Class<?> serviceClass = ServiceManager.getService(serviceToLaunch);

            // instanciate
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // list all services

    }
}
