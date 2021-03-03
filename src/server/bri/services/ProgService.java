package server.bri.services;

import server.bri.loaders.ServiceLoader;
import server.bri.managers.BRIManager;
import server.bri.managers.ServiceManager;
import server.bri.managers.UserManager;
import utils.NetworkData;

import java.net.Socket;

public class ProgService implements Runnable {
    private final NetworkData net;
    private UserManager userManager;

    public ProgService(Socket socket) {
        net = new NetworkData(socket);
        new Thread(this).start();
    }

    @Override
    public void run() {
        // ask to login
        net.send("Please you have to be logged in ! Enter you login pwd and server url. Follow this model -> [login]&[password]&[ftp server url]");

        String credentials = net.read().toString();
        boolean notConform;

        // login verification
        do {
            try {
                Object[] credentialsVerified = verifyCredentials(credentials);
                notConform = !(boolean) credentialsVerified[0];
                String[] credentialsSplit = (String[]) credentialsVerified[1];
                this.userManager = UserManager.getInstance();
                BRIManager.login(credentialsSplit[0], credentialsSplit[1], credentialsSplit[2]);
            } catch (Exception e) {
                net.send(e.getMessage());
                notConform = true;
            }
        }while(notConform);

        // list all features available
        String messageToSend = """
                Welcome to the BRI manager for incredible programmers !
                You can do these following actions :
                \t- Install a service from you ftp server [1]
                \t- Start a service [2]
                \t- Stop a service [3]
                \t- Update a service [4]
                \t- Uninstall a service [5]""";

        boolean stop;
        do {
            net.send(messageToSend);
            String choice = net.read().toString();

            stop = choice.equals("stop");

            int choiceInteger = Integer.parseInt(choice);

            switch(choiceInteger) {
                case 1 -> installService();
                case 2 -> startService();
                case 3 -> stopService();
                case 4 -> update();
                case 5 -> uninstall();
                default -> net.send("This choice doesn't exist");
            }
        }while(!stop);


    }

    public void installService() {
        try {
            ServiceLoader serviceLoader = new ServiceLoader(userManager.getCurrentDev().getFtpUrl());

            net.send("Please enter the path ?");
            String path = net.read().toString();

            Class<?> classLoaded = serviceLoader.loadClass(path);
            BRIManager.installService(classLoaded);
            net.send("Service was added successfully");
        } catch (Exception e) {
            net.send("Error -> " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void startService() {
        String message = "Choose the service to start in this list:\n\t" + BRIManager.getStoppedClassesListing();
        net.send(message);
        try {
            int choice = Integer.parseInt(net.read().toString());
            BRIManager.startService(choice);
            net.send("Service started successfully ");
        } catch (Exception e) {
            net.send("Error -> " + e.getMessage());
        }
    }

    public void stopService() {
        String message = "Choose the service to stop in this list:\n\t" + BRIManager.getStartedClassesListing();
        net.send(message);
        try {
            int choice = Integer.parseInt(net.read().toString());
            BRIManager.stopService(choice);
            net.send("Service sopped successfully ");
        } catch (Exception e) {
            net.send("Error -> " + e.getMessage());
        }
    }
    
    public void update(){
        String message = "Choose the service to update within in this list:\n\t" + ServiceManager.serviceListing();
        net.send(message);

        try {
            int choice = Integer.parseInt(net.read().toString());
            net.send("Enter the path to the updated class");
            String path = net.read().toString();

            ServiceLoader serviceLoader = new ServiceLoader(userManager.getCurrentDev().getFtpUrl());
            Class<?> bean = serviceLoader.loadUpdatedClass(path);
            BRIManager.updateService(bean, choice);
            net.send("Service updated successfully ");

        }catch (Exception e ) {
            net.send(e.getMessage());
        }
    }
    
    public void uninstall() {
        String message = "Choose the service in this list:\n\t" + ServiceManager.serviceListing(); //TODO
        net.send(message);
        try {
            int choice = Integer.parseInt(net.read().toString());
            BRIManager.uninstallService(choice);
            net.send("Service uninstalled successfully ");
        } catch (Exception e) {
            net.send("Error -> " + e.getMessage());
        }
    }

    private Object[] verifyCredentials(String credentials) throws Exception{
        String[] credentialsSplit = credentials.split("&");

        if(credentialsSplit[0].isEmpty() || credentialsSplit[1].isEmpty() || credentialsSplit[2].isEmpty())
            throw new Exception("You have to fill all fields");
        if(!credentialsSplit[2].startsWith("ftp://"))
            throw new Exception("Your url have to follow this pattern -> ftp://[server url]:[port]");

        return new Object[]{true, credentialsSplit};
    }
}
