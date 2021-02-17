package server.bri;

import java.util.Vector;

public class ServiceRegistry {
    private static Vector<Class<?>> serviceClassList;

    static {
        serviceClassList = new Vector<>();
    }

    public static void addService(Class<?> serviceClass, String ftpServer){}

    public static Class<?> getService(int numService){
        return null;
    }

    public static void deleteService(int numService){}

    public static String serviceListing() {
        StringBuilder sb = new StringBuilder("");
        sb.append("Services available: \t");
        serviceClassList.forEach(s -> sb.append(s.getName()).append("\n"));
        return sb.replace(sb.length() -1, sb.length(), "").toString();
    }
}
