package server.bri.loaders;

import server.bri.managers.ServiceManager;
import server.model.Developer;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

public class ClassLoader extends URLClassLoader {
    private final Developer dev;

    public ClassLoader(Developer dev, String url) throws MalformedURLException {
        super(new URL[]{new URL(url)});
        this.dev = dev;
    }

    @Override
    public Class<?> loadClass(String classPath) throws ClassNotFoundException {
        Iterator<Class<?>> services = ServiceManager.getServiceIterator();
        while (services.hasNext()) {
            String login = dev.getLogin();
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
