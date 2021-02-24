package server.bri.managers;

import server.model.Developer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class BRIManager {
    private static final UserManager userManager;

    private static final List<Class<?>> startedClasses;
    private static final List<Class<?>> stoppedClasses;
    private static final Map<Developer, Vector<Class<?>>> classesDictionnary;

    static {
        userManager = UserManager.getInstance();
        try {
            Class.forName("server.bri.managers.ServiceManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startedClasses = new Vector<>();
        stoppedClasses = new Vector<>();
        classesDictionnary = new HashMap<>();
    }

    public static boolean isNotKnownService(Class<?> bean) {
        return !ServiceManager.isAvailable(bean);
    }

    private static boolean isAuthorizedToPerformAction(Class<?> bean){
        Vector<Class<?>> serviceVector = classesDictionnary.get(userManager.getCurrentDev());
        if(serviceVector == null)
            return false;
        return serviceVector.contains(bean);
    }

    public static void installService(Class<?> bean) throws Exception {
        ServiceManager.addService(bean);
        addToDictionary(bean);
    }

    public static boolean stopService(int numService) {
        Class<?> bean = ServiceManager.getService(numService);
        if(!isAuthorizedToPerformAction(bean))
            return false;
        if(isNotKnownService(bean))
            return false;
        return stoppedClasses.add(bean);
    }

    public static boolean startService(int numService) {
        Class<?> bean = ServiceManager.getService(numService);
        if(!isAuthorizedToPerformAction(bean))
            return false;
        if(isNotKnownService(bean))
            return false;
        stoppedClasses.remove(bean);
        return startedClasses.add(bean);
    }

    public static boolean uninstallService(int numService) {
        Class<?> bean = ServiceManager.getService(numService);
        if(!isAuthorizedToPerformAction(bean))
            return false;
        if(isNotKnownService(bean))
            return false;
        stoppedClasses.remove(bean);
        startedClasses.remove(bean);
        ServiceManager.deleteService(numService);
        return true;
    }

    private static void addToDictionary(Class<?> bean) {
        if(!classesDictionnary.containsKey(userManager.getCurrentDev())) {
            Vector<Class<?>> classes = new Vector<>();
            classes.add(bean);
            classesDictionnary.put(userManager.getCurrentDev(), classes);
        }
        else {
            classesDictionnary.get(userManager.getCurrentDev()).add(bean);
        }
    }

    public static void login(String login, String password, String ftpUrl) throws IllegalAccessException {
        userManager.login(login, password, ftpUrl);
    }
}
