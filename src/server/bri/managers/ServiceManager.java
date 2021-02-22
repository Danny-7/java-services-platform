package server.bri.managers;

import server.bri.tools.ValidateService;

import java.util.Vector;

public class ServiceManager {
    private static final Vector<Class<?>> serviceClassList;

    static {
        serviceClassList = new Vector<>();
    }

    public static void addService(Class<?> serviceClass) throws Exception {
        if(serviceClassList.contains(serviceClass))
            throw new IllegalArgumentException("This service class already exists !");
        ValidateService.isConform(serviceClass);
        serviceClassList.add(serviceClass);
    }

    public static Class<?> getService(int numService){
        int num = numService -1;
        if(serviceClassList.isEmpty() || num > serviceClassList.size())
            return null;
        return serviceClassList.get(num);
    }

    public static void deleteService(int numService){
        int num = numService -1;
        if(!serviceClassList.isEmpty() && num < serviceClassList.size())
            serviceClassList.remove(num);
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
