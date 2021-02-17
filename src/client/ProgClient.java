package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ProgClient {
    private static Socket socket;
    private static final String HOST = "localhost";
    private static final int PORT = 4000;

    public static void main(String[] args) {
        try {
            socket = new Socket(HOST, PORT);
            ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);

            System.out.format("You are connected on port %d at %s", PORT, HOST);

            while(true) {
                String message = (String) socketIn.readObject();
                System.out.println(message);
            }


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
