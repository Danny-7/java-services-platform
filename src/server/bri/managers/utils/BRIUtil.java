package server.bri.managers.utils;

import java.util.List;

public class BRIUtil {

    public static String getListing(List<Class<?>> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(s -> sb.append(s.getSimpleName()).append("\n\t"));
        if(!sb.isEmpty())
            sb.replace(sb.length() -2, sb.length(), "");
        return sb.toString();
    }
}
