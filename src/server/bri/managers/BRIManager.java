package server.bri.managers;

import server.bri.managers.utils.BRIUtil;
import server.model.Developer;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main manager of the application, contains list of classes owns by developers
 */
public class BRIManager {
    private static final Map<Developer, Vector<Class<?>>> classesDictionary;

    static {
        classesDictionary = new ConcurrentHashMap<>();
    }

    public static boolean isNotKnownService(Class<?> bean) {
        return !ServiceManager.isAvailable(bean);
    }

    private static boolean isNotAuthorizedToPerformAction(Developer dev, Class<?> bean) {
        Vector<Class<?>> serviceVector = classesDictionary.get(dev);
        if (serviceVector == null)
            return true;
        return !serviceVector.contains(bean);
    }

    public static void installService(Developer dev, Class<?> bean) throws Exception {
        ServiceManager.addService(dev, bean);
        addToDictionary(dev, bean);
    }

    /**
     * Stop a service and this service will be store in a hashmap with the position in the service class list
     *
     * @param numService index of the service in the list displayed to the client
     */
    public static void stopService(Developer dev, int numService) throws Exception {
        Class<?> bean = ServiceManager.getStartedService(numService);
        if (isNotAuthorizedToPerformAction(dev, bean))
            throw new Exception("You are not allowed to perform this action !");
        if (isNotKnownService(bean))
            throw new Exception("This number doesn't exist !");
        ServiceManager.stopService(bean);
    }

    public static void startService(Developer dev, int numService) throws Exception {
        Class<?> bean = ServiceManager.getStoppedService(numService);
        if (isNotAuthorizedToPerformAction(dev, bean))
            throw new Exception("You are not allowed to perform this action !");
        if (isNotKnownService(bean))
            throw new Exception("This number doesn't exist !");
        ServiceManager.startService(bean);
    }

    public static void uninstallService(Developer dev, int numService) throws Exception {
        Class<?> bean = ServiceManager.getService(numService);
        if (isNotAuthorizedToPerformAction(dev, bean))
            throw new Exception("You are not allowed to perform this action !");
        if (isNotKnownService(bean))
            throw new Exception("This number doesn't exist !");
        ServiceManager.deleteService(numService);
    }

    public static void updateService(Developer dev, Class<?> beanUpdated, int numService) throws Exception {
        Class<?> bean = ServiceManager.getService(numService);
        if (isNotAuthorizedToPerformAction(dev, bean))
            throw new Exception("You are not allowed to perform this action !");
        if (isNotKnownService(bean))
            throw new Exception("This number doesn't exist !");
        ServiceManager.updateService(bean, beanUpdated, numService);
        updateDictionary(dev, bean, beanUpdated);
    }

    private static void addToDictionary(Developer dev, Class<?> bean) {
        if (!classesDictionary.containsKey(dev)) {
            Vector<Class<?>> classes = new Vector<>();
            classes.add(bean);
            classesDictionary.put(dev, classes);
        } else
            classesDictionary.get(dev).add(bean);
    }

    private static void updateDictionary(Developer dev, Class<?> bean, Class<?> beanUpdated) {
        int beanIndex = classesDictionary.get(dev).indexOf(bean);
        classesDictionary.get(dev).setElementAt(beanUpdated, beanIndex);
    }

    public static String getStoppedClassesListing() {
        return BRIUtil.getListing(ServiceManager.getStoppedClasses());
    }

    public static String getStartedClassesListing() {
        return BRIUtil.getListing(ServiceManager.getStartedClasses());
    }
}
