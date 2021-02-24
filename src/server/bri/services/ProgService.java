package server.bri.services;

import server.bri.NetworkData;
import server.bri.Service;
import server.bri.loaders.ServiceLoader;
import server.bri.managers.BRIManager;
import server.bri.managers.ServiceManager;
import server.bri.managers.UserManager;

import java.net.Socket;

public class ProgService implements Service {
    private final NetworkData net;
    private UserManager userManager;

    public ProgService(Socket socket) { net = new NetworkData(socket); }
    @Override
    public void run() {
        // ask to login
        net.send("Please you have to be logged in ! Enter you login pwd and server url. Follow this model -> [login]&[password]&[ftp server url]");

        String credentials = net.read().toString();
        boolean notConform;

        try {
            Class.forName("server.bri.managers.UserManager");
            Class.forName("server.bri.managers.BRIManager");
        } catch (ClassNotFoundException e) {
            net.send(e.getMessage());
        }

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
        boolean stop = false;

        String messageToSend = """
                Welcome to the BRI manager for incredible programmers !
                You can do these following actions :
                \t- Install a service from you ftp server [1]
                \t- Start a service [2]
                \t- Stop a service [3]
                \t- Update a service [4]
                \t- Uninstall a service [5]""";

        do {
            net.send(messageToSend);
            String choice = net.read().toString();

            if(choice.equals("stop")) {
                stop = true;
                break;
            }

            int choiceInteger = Integer.parseInt(choice);

            switch(choiceInteger) {
                case 1:
                    installService();
                    break;
                case 2:
                    startService();
                    break;
                case 3:
                    stopService();
                case 4:
                    update();
                    break;
                case 5:
                    uninstall();
                    break;
                default:
                    net.send("This choice doesn't exist");
            }
        }while(!stop);


    }

    public void installService() {
        try {
            ServiceLoader serviceLoader = new ServiceLoader(userManager.getCurrentDev().getFtpUrl());
            System.out.println(serviceLoader);

            net.send("Please enter the path ?");
            String path = (String) net.read();

            Class<?> classLoaded = serviceLoader.loadClass(path);
            BRIManager.installService(classLoaded);
//            net.send("Service "+ classLoaded.getSimpleName() + " added");
        } catch (Exception e) {
            net.send(e.getMessage());
        }
    }

    public void startService() {
        String message = "Choose the service in this list:\n\t" + ServiceManager.serviceListing(); //TODO
        net.send(message);
        int choice = (int) net.read();
        BRIManager.startService(choice);
    }

    public void stopService() {
        String message = "Choose the service in this list:\n\t" + ServiceManager.serviceListing(); //TODO
        net.send(message);
        int choice = (int) net.read();
        BRIManager.stopService(choice);
    }
    
    public void update(){
        // TODO
    }
    
    public void uninstall() {
            String message = "Choose the service in this list:\n\t" + ServiceManager.serviceListing(); //TODO
            net.send(message);
            int choice = (int) net.read();
            BRIManager.uninstallService(choice);
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
