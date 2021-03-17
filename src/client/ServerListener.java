package client;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Server listener for catch incoming messages from server
 */
public class ServerListener implements Runnable {
    private final ObjectInputStream socketIn;

    public ServerListener(ObjectInputStream socketIn) {
        this.socketIn = socketIn;
        new Thread(this).start();
    }

    @Override
    public void run() {
        boolean isActive = true;
        while (isActive) {
            String serverMessage;
            try {
                // read serializable data sent by the server
                serverMessage = socketIn.readObject().toString();
                System.out.println(serverMessage);
            } catch (IOException | ClassNotFoundException e) {
                isActive = false;
            }
        }
    }
}
