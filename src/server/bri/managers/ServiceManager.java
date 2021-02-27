package server.bri.managers;

import server.bri.tools.ValidateService;

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
     * @param serviceClass class to be added
     * @throws Exception Return an exception if the class already exists or if it's not conform
     */
    public static void addService(Class<?> serviceClass) throws Exception {
        if(stoppedClasses.contains(serviceClass) || startedClasses.contains(serviceClass))
            throw new IllegalArgumentException("This service class already exists !");
        ValidateService.isConform(serviceClass);
        stoppedClasses.add(serviceClass);
        serviceClassList.add(serviceClass);
    }

    public static Class<?> getService(int numService) {
        int num = numService -1;
        if(serviceClassList.isEmpty() || num > serviceClassList.size())
            return null;
        return serviceClassList.get(num);
    }

    public static Class<?> getStartedService(int numService){
        int num = numService -1;
        if(startedClasses.isEmpty() || num > startedClasses.size())
            return null;
        return startedClasses.get(num);
    }

    public static Class<?> getStoppedService(int numService){
        int num = numService -1;
        if(stoppedClasses.isEmpty() || num > stoppedClasses.size())
            return null;
        return stoppedClasses.get(num);
    }

    public static void deleteService(int numService) throws Exception {

        int num = numService -1;
        if(serviceClassList.isEmpty() || num > serviceClassList.size())
            throw new Exception("This number is not associated to a known service");
        Class<?> bean = serviceClassList.get(num);
        stoppedClasses.remove(bean);
        startedClasses.remove(bean);
    }

    public static void stopService(Class<?> bean) {
        startedClasses.remove(bean);
        stoppedClasses.add(bean);
    }

    public static void startService(Class<?> bean) {
        stoppedClasses.remove(bean);
        startedClasses.add(bean);
    }

    public static void updateService(Class<?> bean, int index) {
        // TODO verify equals method of a class
        serviceClassList.setElementAt(bean, index - 1);
        if(stoppedClasses.contains(bean)) {
            int beanIndex = stoppedClasses.indexOf(bean);
            stoppedClasses.setElementAt(bean, beanIndex);
        }
        else if(startedClasses.contains(bean)) {
            int beanIndex = stoppedClasses.indexOf(bean);
            stoppedClasses.setElementAt(bean, beanIndex);
        }

    }

    public static boolean isAvailable(Class<?> serviceClass) {
        return serviceClassList.contains(serviceClass);
    }

    public static String serviceListing() {
        StringBuilder sb = new StringBuilder();
        sb.append("Services available: \t");
        serviceClassList.forEach(s -> sb.append(s.getSimpleName()).append("\n"));
        return sb.replace(sb.length() -1, sb.length(), "").toString();
    }
}
