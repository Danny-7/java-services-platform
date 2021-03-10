package server.bri.loaders;

import server.bri.managers.ServiceManager;
import server.bri.managers.UserManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

public class ClassLoader extends URLClassLoader {

    public ClassLoader(String url) throws MalformedURLException {
        super(new URL[]{new URL(url)});
    }

    @Override
    public Class<?> loadClass(String classPath) throws ClassNotFoundException {
        Iterator<Class<?>> services = ServiceManager.getServiceIterator();
        while (services.hasNext()) {
            String login = UserManager.getInstance().getCurrentDev().getLogin();
            String actualClassPath = login + "." + services.next().getSimpleName();
            if (actualClassPath.equals(classPath)) {
                return findClass(classPath);
            }
        }
        return super.loadClass(classPath);
    }

    @Override
    public String toString() {
        return "ClassLoader";
    }
}
