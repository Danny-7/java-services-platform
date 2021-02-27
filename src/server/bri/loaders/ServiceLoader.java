package server.bri.loaders;

import server.bri.managers.ServiceManager;
import server.bri.managers.UserManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Iterator;

public class ServiceLoader{
    private final URLClassLoader urlClassLoader;
    private URLClassLoader urlUpdateClassLoader;

    private final String classURL;

    public ServiceLoader(String classURL) throws MalformedURLException {
        urlClassLoader = new URLClassLoader(new URL[]{new URL(classURL)});
        this.classURL = classURL;
        urlUpdateClassLoader = null;
    }

    public Class<?> loadClass(String classPath) throws ClassNotFoundException {
        return urlClassLoader.loadClass(classPath);
    }

    public Class<?> loadUpdatedClass(String classPath) throws MalformedURLException, ClassNotFoundException {
        urlUpdateClassLoader =  new URLClassLoader(new URL[]{new URL(this.classURL)}) {
            public Class<?> loadClass(String classPath) throws ClassNotFoundException {
                Iterator<Class<?>> services = ServiceManager.getServiceIterator();
                while(services.hasNext()) {
                    String login = UserManager.getInstance().getCurrentDev().getLogin();
                    String actualClassPath = login + "." + services.next().getSimpleName();
                    if(actualClassPath.equals(classPath)) {
                        return findClass(classPath);
                    }
                }
                return super.loadClass(classPath);
            }
        };
        return urlUpdateClassLoader.loadClass(classPath);
    }

    @Override
    public String toString() {
        return "ftp url -> " + Arrays.toString(urlClassLoader.getURLs());
    }
}
