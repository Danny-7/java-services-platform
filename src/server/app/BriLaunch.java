package server.app;

import server.bri.Server;

import java.util.Scanner;

public class BriLaunch {

    public static void main(String[] args) {
        Scanner line = new Scanner(System.in);

        String displayedMessage = """
                Welcome to your bri manager !
                """;

        System.out.println(displayedMessage);

        new Thread(new Server(4000)).start();

        while (true) {
            String response = line.nextLine();

        }
    }
}
