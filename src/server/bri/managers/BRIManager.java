package server.bri.managers;

import server.model.Developer;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class BRIManager {
    private static final UserManager userManager;
    private static final Map<Developer, Vector<Class<?>>> classesDictionary;

    static {
        userManager = UserManager.getInstance();
        try {
            Class.forName("server.bri.managers.ServiceManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        classesDictionary = new HashMap<>();
    }

    public static boolean isNotKnownService(Class<?> bean) {
        return !ServiceManager.isAvailable(bean);
    }

    private static boolean isAuthorizedToPerformAction(Class<?> bean){
        Vector<Class<?>> serviceVector = classesDictionary.get(userManager.getCurrentDev());
        if(serviceVector == null)
            return false;
        return serviceVector.contains(bean);
    }

    public static void installService(Class<?> bean) throws Exception {
        ServiceManager.addService(bean);
        addToDictionary(bean);
    }

    /**
     * Stop a service and this service will be store in a hashmap with the position in the service class list
     * @param numService index of the service in the list displayed to the client
     */
    public static void stopService(int numService) throws Exception {
        Class<?> bean = ServiceManager.getStartedService(numService);
        if(!isAuthorizedToPerformAction(bean))
            throw new Exception("You are not allowed to perform this action !");
        if(isNotKnownService(bean))
            throw new Exception("This number doesn't exist !");
        ServiceManager.stopService(bean);
    }

    public static void startService(int numService) throws Exception {
        Class<?> bean = ServiceManager.getStoppedService(numService);
        if(!isAuthorizedToPerformAction(bean))
            throw new Exception("You are not allowed to perform this action !");
        if(isNotKnownService(bean))
            throw new Exception("This number doesn't exist !");
        ServiceManager.startService(bean);
    }

    public static void uninstallService(int numService) throws Exception {
        Class<?> bean = ServiceManager.getService(numService);
        if(!isAuthorizedToPerformAction(bean))
            throw new Exception("You are not allowed to perform this action !");
        if(isNotKnownService(bean))
            throw new Exception("This number doesn't exist !");
        ServiceManager.deleteService(numService);
    }

    public static void updateService(Class<?> beanUpdated, int numService) throws Exception {
        Class<?> bean = ServiceManager.getService(numService);
        if(!isAuthorizedToPerformAction(bean))
            throw new Exception("You are not allowed to perform this action !");
        if(isNotKnownService(bean))
            throw new Exception("This number doesn't exist !");
        ServiceManager.updateService(beanUpdated, numService);
    }

    private static void addToDictionary(Class<?> bean) {
        if(!classesDictionary.containsKey(userManager.getCurrentDev())) {
            Vector<Class<?>> classes = new Vector<>();
            classes.add(bean);
            classesDictionary.put(userManager.getCurrentDev(), classes);
        }
        else
            classesDictionary.get(userManager.getCurrentDev()).add(bean);
    }

    public static void login(String login, String password, String ftpUrl) throws IllegalAccessException {
        userManager.login(login, password, ftpUrl);
    }

    public static String getStoppedClassesListing() {
        StringBuilder sb = new StringBuilder();
        ServiceManager.getStoppedClasses().forEach(s -> sb.append(s.getSimpleName()).append("\n"));
        return sb.replace(sb.length() -1, sb.length(), "").toString();
    }

    public static String getStartedClassesListing() {
        StringBuilder sb = new StringBuilder();
        ServiceManager.getStartedClasses().forEach(s -> sb.append(s.getSimpleName()).append("\n"));
        return sb.replace(sb.length() -1, sb.length(), "").toString();
    }
}
