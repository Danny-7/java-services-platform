package server.bri.services;


import server.bri.managers.BRIManager;
import server.bri.managers.ServiceManager;
import utils.*;

import java.net.Socket;

public class AmaService implements Runnable {
    private final NetworkData net;
    private final Socket socket;

    public AmaService(Socket socket) {
        net = new NetworkData(socket);
        this.socket = socket;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            String classes = BRIManager.getStartedClassesListing();
            String services = "Choose a service to use :\n\t" + classes;
            net.send(services);

            int serviceToLaunch = Integer.parseInt(net.read().toString());

            Class<?> serviceClass = ServiceManager.getService(serviceToLaunch);
            // service instantiation
            if(serviceClass == null)
                throw new RuntimeException("Can't use the service asked");

            Object[] initArgs = new Object[]{socket, net};
            Service service = (Service) serviceClass
                    .getConstructor(Socket.class, NetworkData.class).newInstance(initArgs);
            service.run();

        } catch (ReflectiveOperationException | RuntimeException e) {
            net.send(e.getMessage());
        }
    }
}
