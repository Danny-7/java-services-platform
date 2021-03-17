package server.bri.managers;

import server.model.Developer;

import java.util.Vector;

/**
 * Utility class for authentication
 */
public class DeveloperManager {
    private static final Vector<Developer> developers;

    static {
        developers = new Vector<>();
        developers.add(new Developer("danny", "azerty", "ftp://localhost:2121/"));
        developers.add(new Developer("remy", "azerty", "ftp://localhost:2121/"));
    }

    private static int hasDeveloper(Developer dev) {
        return developers.indexOf(dev);
    }

    public static Developer login(String login, String pwd, String ftpServer) throws IllegalAccessException {
        Developer dev = new Developer(login, pwd, ftpServer);
        int indexDev = hasDeveloper(dev);
        if (indexDev == -1)
            throw new IllegalAccessException("This developer doesn't exist");
        Developer devAuth = developers.stream()
                .filter(developer -> developer.equals(dev) & developer.isAuthenticated())
                .findFirst().orElse(null);
        if (devAuth != null)
            throw new IllegalAccessException("You are already connected as " + devAuth.getLogin());
        dev.setAuthenticated(true);
        developers.setElementAt(dev, indexDev);
        return dev;
    }
}
