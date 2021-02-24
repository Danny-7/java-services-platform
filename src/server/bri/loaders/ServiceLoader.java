package server.bri.loaders;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class ServiceLoader{
    private final URLClassLoader urlClassLoader;

    public ServiceLoader(String classURL) throws MalformedURLException {
        urlClassLoader = new URLClassLoader(new URL[]{new URL(classURL)});
    }

    public Class<?> loadClass(String classPath) throws ClassNotFoundException {
        return urlClassLoader.loadClass(classPath);
    }

    @Override
    public String toString() {
        return "ftp url -> " + Arrays.toString(urlClassLoader.getURLs());
    }
}
