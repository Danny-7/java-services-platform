package client;


import utils.NetworkData;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class AmaClient{
    private static Socket socket;
    private static final String HOST = "localhost";
    private static final int PORT = 3000;

    public static void main(String[] args) {
        NetworkData net = null;
        try {
            socket = new Socket(HOST, PORT);
            net = new NetworkData(socket);
            Scanner sc = new Scanner(System.in);

            System.out.format("You are connected on port %d at %s\n", PORT, HOST);

            while(true) {
                String message = net.read().toString();
                System.out.println(message);
                String answer = sc.nextLine();
                net.send(answer);
            }
        } catch (IOException e) {
            net.send(e.getLocalizedMessage());
        }
    }
}
