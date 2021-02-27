package server.bri.services;


import server.bri.NetworkData;
import server.bri.Service;
import server.bri.managers.BRIManager;
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
            String services = "Choose a service to use :\n\t" + BRIManager.getStartedClassesListing();
            net.send(services);

            int serviceToLaunch = Integer.parseInt(net.read().toString());

            Class<?> serviceClass = ServiceManager.getService(serviceToLaunch);
            // instantiation of the service
            Service service = (Service) serviceClass.newInstance();
            new Thread(service).start();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            net.send(e.getMessage());
            e.printStackTrace();
        }
    }
}
