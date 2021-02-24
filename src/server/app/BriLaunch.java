package server.app;

import server.bri.Server;

public class BriLaunch {

    public static void main(String[] args) {

        String displayedMessage = " Welcome to your bri manager !";

        System.out.println(displayedMessage);

        new Thread(new Server(4000)).start();
    }
}
