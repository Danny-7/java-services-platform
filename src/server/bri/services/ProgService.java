package server.bri.services;

import server.bri.loaders.ClassLoader;
import server.bri.managers.BRIManager;
import server.bri.managers.ProgServiceManager;
import server.bri.managers.ServiceManager;
import server.bri.managers.DeveloperManager;
import server.model.Developer;
import utils.NetworkData;

import java.net.Socket;

/**
 * Service for developers who want to install, uninstall, update, start, stop services own
 */
public class ProgService implements Runnable, AutoCloseable {
    private static final String MSG_NO_SERVICE = "There's any available services !";
    private final NetworkData net;
    private Developer developer;
    private ProgServiceManager progServiceManager;
    private final Socket socket;

    public ProgService(Socket socket) {
        net = new NetworkData(socket);
        this.socket = socket;
        this.developer = null;
        this.progServiceManager = null;
        new Thread(this).start();
    }

    @Override
    public void run() {
        // ask to login
        net.send("Please you have to be logged in ! Enter you login pwd and server url. Follow this model -> [login]&[password]&[ftp server url]");

        String credentials;
        boolean notConform;

        // login verification
        do {
            try {
                credentials = net.read().toString();
                notConform = authenticate(credentials);
            } catch (Exception e) {
                net.send(e.getMessage());
                net.send("Try again");
                notConform = true;
            }
        } while (notConform);

        net.send("Welcome to the BRI manager for incredible programmers !");

        this.progServiceManager = new ProgServiceManager(this.developer);
        // list all features available
        String messageToSend = """
                        
        You can do these following actions :
        \t- Install a service from you ftp server [1]
        \t- Start a service [2]
        \t- Stop a service [3]
        \t- Update a service [4]
        \t- Uninstall a service [5]
        \t- Modify url ftp server[6]""";

        boolean stop;
        do {
            net.send(messageToSend);
            String choice = net.read().toString();

            stop = choice.equals("stop");

            int choiceInteger = Integer.parseInt(choice);

            switch (choiceInteger) {
                case 1 -> installService();
                case 2 -> startService();
                case 3 -> stopService();
                case 4 -> update();
                case 5 -> uninstall();
                case 6 -> modifyServerUrl();
                default -> net.send("This choice doesn't exist");
            }
            System.out.println(ServiceManager.serviceListing());
        } while (!stop);
    }

    public boolean authenticate(String credentials) throws Exception {
        boolean notConform;
        Object[] credentialsVerified = verifyCredentials(credentials);
        notConform = !(boolean) credentialsVerified[0];
        String[] credentialsSplit = (String[]) credentialsVerified[1];
        this.developer = DeveloperManager
                .login(credentialsSplit[0], credentialsSplit[1], credentialsSplit[2]);
        return notConform;
    }

    public void installService() {
        try {
            net.send("What's the type of your service ? [.class, .jar]");
            String fileType = net.read().toString();

            boolean JARType = fileType.equals(".jar");

            String classURL = this.developer.getFtpUrl();
            if (JARType) {
                net.send("Please enter the path to the JAR file ?");
                String pathToJAR = net.read().toString();
                classURL += pathToJAR;
            }
            progServiceManager.installService(loadService("Enter the service to load", classURL));
            net.send("Service was added successfully");
        } catch (Exception e) {
            net.send("Error -> " + e.getMessage());
        }
    }

    public Class<?> loadService(String messageToDisplay, String classUrl) throws Exception {
        net.send(messageToDisplay);
        String classToLoad = net.read().toString();
        ClassLoader serviceLoader = new ClassLoader(this.developer, classUrl);
        return serviceLoader.loadClass(classToLoad);
    }

    public void startService() {
        String startedServices = BRIManager.getStoppedClassesListing();

        try {
            if (startedServices.isBlank())
                throw new Exception(MSG_NO_SERVICE);
            String message = "Choose the service to start in this list:\n\t" + startedServices;
            net.send(message);

            int choice = Integer.parseInt(net.read().toString());
            progServiceManager.startService(choice);
            net.send("Service started successfully ");
        } catch (Exception e) {
            net.send("Error -> " + e.getMessage());
        }
    }

    public void stopService() {
        String stoppedServices = BRIManager.getStartedClassesListing();

        try {
            if (stoppedServices.isBlank())
                throw new Exception(MSG_NO_SERVICE);
            String message = "Choose the service to stop in this list:\n\t" + stoppedServices;
            net.send(message);

            int choice = Integer.parseInt(net.read().toString());
            progServiceManager.stopService(choice);
            net.send("Service sopped successfully ");
        } catch (Exception e) {
            net.send("Error -> " + e.getMessage());
        }
    }

    public void update() {
        String services = ServiceManager.serviceListing();
        try {
            if (services.isBlank())
                throw new Exception(MSG_NO_SERVICE);
            String message = "Choose the service to update within in this list:\n\t" + services;
            net.send(message);

            int choice = Integer.parseInt(net.read().toString());

            net.send("What's the type of your service ? [.class, .jar]");
            String fileType = net.read().toString();
            boolean JARType = fileType.equals(".jar");

            String classURL = this.developer.getFtpUrl();
            if (JARType) {
                net.send("Please enter the path to the JAR file ?");
                String pathToJAR = net.read().toString();
                classURL += pathToJAR;
            }

            progServiceManager
                    .updateService(
                            loadService("Enter the path to the updated service", classURL), choice);
            net.send("Service updated successfully ");

        } catch (Exception e) {
            net.send("Error -> " + e.getMessage());
        }
    }

    public void uninstall() {
        String services = ServiceManager.serviceListing();
        try {
            if (services.isBlank())
                throw new Exception(MSG_NO_SERVICE);
            String message = "Choose the service in this list:\n\t" + services;
            net.send(message);

            int choice = Integer.parseInt(net.read().toString());
            progServiceManager.uninstallService(choice);
            net.send("Service uninstalled successfully ");
        } catch (Exception e) {
            net.send("Error -> " + e.getMessage());
        }
    }

    public void modifyServerUrl() {
        try {
            if (this.developer == null)
                throw new IllegalStateException("You can't do this action because you are not logged");
            net.send("Enter you new ftp url server");
            String newUrl = net.read().toString();
            if (newUrl.isBlank())
                throw new IllegalAccessException("Please enter a valid url");
            this.developer.setFtpUrl(newUrl);

        } catch (Exception e) {
            net.send("Error -> " + e.getMessage());
        }
    }

    private Object[] verifyCredentials(String credentials) throws Exception {
        String[] credentialsSplit = credentials.split("&");
        int nbArgs = 3;

        if (credentialsSplit.length != nbArgs)
            throw new Exception("You have to give three arguments separated by '&'");
        if (credentialsSplit[0].isEmpty() || credentialsSplit[1].isEmpty() || credentialsSplit[2].isEmpty())
            throw new Exception("You have to fill all fields");
        if (!credentialsSplit[2].startsWith("ftp://"))
            throw new Exception("Your url have to follow this pattern -> ftp://[server url]:[port]");

        return new Object[]{true, credentialsSplit};
    }

    @Override
    public void close() throws Exception {
        if(!socket.isClosed())
            socket.close();
    }
}
