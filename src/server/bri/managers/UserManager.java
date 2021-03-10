package server.bri.managers;

import server.model.Developer;

import java.util.Vector;

public class UserManager {
    private static Vector<Developer> developers;
    private static UserManager instance;

    static {
        instance = new UserManager();
    }

    private Developer currentDev;

    private UserManager() {
        developers = new Vector<>();
        developers.add(new Developer("danny", "azerty", "ftp://localhost:2121/"));
    }

    public static UserManager getInstance() {
        return instance;
    }

    private static boolean isDeveloper(Developer dev) {
        return developers.contains(dev);
    }

    public Developer getCurrentDev() {
        return currentDev;
    }

    public void login(String login, String pwd, String ftpServer) throws IllegalAccessException {
        Developer dev = new Developer(login, pwd, ftpServer);
        if (currentDev != null && currentDev.equals(dev))
            throw new IllegalAccessException("You are already connected as " + currentDev.getLogin());
        if (!isDeveloper(dev))
            throw new IllegalAccessException("This developer doesn't exist");
        currentDev = dev;
    }
}
