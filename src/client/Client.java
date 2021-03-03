package client;

import utils.NetworkData;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        try {
            Socket socket = new Socket(HOST, port);
            NetworkData net = new NetworkData(socket);
            Scanner sc = new Scanner(System.in);

            System.out.format("You are connected on port %d at %s\n", port, HOST);

            new ServerListener(net.getIn());
            String answer;
            while(true) {
                while((answer = sc.nextLine()).isEmpty());
                net.send(answer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
