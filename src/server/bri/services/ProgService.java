package server.bri.services;

import server.bri.NetworkData;
import server.bri.Service;

import java.net.Socket;

public class ProgService implements Service {
    private final NetworkData net;

    public ProgService(Socket socket) {
        net = new NetworkData(socket);
    }
    @Override
    public void run() {
        // ask to login
        net.send("Please you have to be logged in ! Enter you login pwd and server url. Follow this model -> [login]/[password]/[server url]");

        String credentials = net.read().toString();
        boolean notConform;

        do {
            try {
                notConform = !verifyCredentials(credentials);
            } catch (Exception e) {
                net.send(e.getMessage());
                notConform = true;
            }
        }while(notConform);


        // list all features available
    }

    private boolean verifyCredentials(String credentials) throws Exception{
        String[] credentialsSplit = credentials.split("/");

        if(credentialsSplit[0].isEmpty() || credentialsSplit[1].isEmpty() || credentialsSplit[2].isEmpty())
            throw new Exception("You have to fill all fields");

        if(!credentialsSplit[2].startsWith("ftp://"))
            throw new Exception("Your url have to follow this pattern -> ftp://[server url]:[port]");

        return true;
    }
}
