package server.bri.managers;

import server.bri.managers.utils.BRIUtil;
import server.bri.tools.ValidateService;
import server.model.Developer;

import java.util.Iterator;
import java.util.Vector;

public class ServiceManager {
    private static final Vector<Class<?>> startedClasses;
    private static final Vector<Class<?>> stoppedClasses;
    private static final Vector<Class<?>> serviceClassList;

    static {
        startedClasses = new Vector<>();
        stoppedClasses = new Vector<>();
        serviceClassList = new Vector<>();
    }

    protected static Vector<Class<?>> getStartedClasses() {
        return startedClasses;
    }

    protected static Vector<Class<?>> getStoppedClasses() {
        return stoppedClasses;
    }

    public static Iterator<Class<?>> getServiceIterator() {
        return serviceClassList.iterator();
    }

    /**
     * A service class is added by default in the stopped services
     *
     * @param serviceClass class to be added
     * @throws Exception Return an exception if the class already exists or if it's not conform
     */
    public static void addService(Developer dev, Class<?> serviceClass) throws Exception {
        if (serviceClassList.stream().anyMatch(c -> c.getName().equals(serviceClass.getName())))
            throw new IllegalArgumentException("This service class already exists !");
        ValidateService validateService = new ValidateService(dev);
        validateService.isConform(serviceClass);
        stoppedClasses.add(serviceClass);
        serviceClassList.add(serviceClass);
    }

    public static Class<?> getService(int numService) {
        int num = numService - 1;
        if (serviceClassList.isEmpty() || num > serviceClassList.size())
            return null;
        return serviceClassList.get(num);
    }

    public static Class<?> getStartedService(int numService) {
        int num = numService - 1;
        if (startedClasses.isEmpty() || num > startedClasses.size())
            return null;
        return startedClasses.get(num);
    }

    public static Class<?> getStoppedService(int numService) {
        int num = numService - 1;
        if (stoppedClasses.isEmpty() || num > stoppedClasses.size())
            return null;
        return stoppedClasses.get(num);
    }

    public static void deleteService(int numService) throws Exception {

        int num = numService - 1;
        if (serviceClassList.isEmpty() || num > serviceClassList.size())
            throw new Exception("This number is not associated to a known service");
        Class<?> bean = serviceClassList.get(num);
        stoppedClasses.remove(bean);
        startedClasses.remove(bean);
        serviceClassList.removeElement(bean);
    }

    public static void stopService(Class<?> bean) {
        startedClasses.remove(bean);
        stoppedClasses.add(bean);
    }

    public static void startService(Class<?> bean) {
        stoppedClasses.remove(bean);
        startedClasses.add(bean);
    }

    public static void updateService(Class<?> beanToReplace, Class<?> bean, int index) {
        serviceClassList.setElementAt(bean, index - 1);
        if (stoppedClasses.stream().anyMatch(c -> c.getName().equals(bean.getName()))) {
            int beanIndex = stoppedClasses.indexOf(beanToReplace);
            stoppedClasses.setElementAt(bean, beanIndex);
        } else if (startedClasses.stream().anyMatch(c -> c.getName().equals(bean.getName()))) {
            int beanIndex = startedClasses.indexOf(beanToReplace);
            startedClasses.setElementAt(bean, beanIndex);
        }
    }

    public static boolean isAvailable(Class<?> serviceClass) {
        return serviceClassList.contains(serviceClass);
    }

    public static String serviceListing() {
        return BRIUtil.getListing(serviceClassList);
    }

}
