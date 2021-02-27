package client;

import server.bri.NetworkData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class AmaClient{
    private static Socket socket;
    private static final String HOST = "localhost";
    private static final int PORT = 3000;

    public static void main(String[] args) {
        try {
            socket = new Socket(HOST, PORT);
            NetworkData net = new NetworkData(socket);
            Scanner sc = new Scanner(System.in);

            System.out.format("You are connected on port %d at %s\n", PORT, HOST);

            while(true) {
                String message = net.read().toString();
                System.out.println(message);
                String answer = sc.next();
                net.send(answer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
