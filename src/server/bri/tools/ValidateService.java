package server.bri.tools;

import server.model.Developer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ValidateService {
    private final Developer dev;

    public ValidateService(Developer dev) {
        this.dev = dev;
    }

    public boolean isConform(Class<?> service) throws Exception {
        Class<?> superclass = service.getSuperclass();
        Constructor<?>[] constructorsMetadata = service.getConstructors();
        Field[] fields = service.getDeclaredFields();
        Method[] methods = service.getMethods();
        int modifiers = service.getModifiers();

        String userLogin = dev.getLogin();
        boolean isRightPackageName = service.getPackageName()
                .equals(this.dev.getLogin());

        if (!isRightPackageName)
            throw new Exception("Your class must be on the " + userLogin + " package");

        boolean hasExtendedServiceClass = superclass.getName().equals("utils.Service");

        if (!hasExtendedServiceClass)
            throw new Exception("You have to extends the Service class on bri_utils package !");

        if (Modifier.isAbstract(modifiers) || !Modifier.isPublic(modifiers))
            throw new Exception("Your service has to be public and not abstract !");

        boolean hasEmptyConformConstructor = Arrays.stream(constructorsMetadata)
                .anyMatch(c -> c.getParameterCount() == 2 && Modifier.isPublic(c.getModifiers())
                        && Arrays.stream(c.getParameters()).anyMatch(p -> p.getType().getName().equals("java.net.Socket")
                        && Arrays.stream(c.getParameters()).anyMatch(param -> param.getType().getName().equals("utils.NetworkData")))
                        && Arrays.asList(c.getExceptionTypes()).isEmpty()
                );

        if (!hasEmptyConformConstructor)
            throw new Exception("Your constructor has to be public with two parameters (Socket, NetworkData) and not throws Exception !");

        Field socketField = Arrays.stream(fields)
                .filter(f -> f.getType().getName().equals("java.net.Socket")).findAny().orElse(null);
        if (socketField == null)
            throw new Exception("Your must have a socket field on your class !");

        int fieldModifiers = socketField.getModifiers();

        if (!Modifier.isFinal(fieldModifiers) || !Modifier.isPrivate(fieldModifiers))
            throw new Exception("Your socket field has to be final and private !");

        boolean hasPublicConformMethod = Arrays.stream(methods)
                .anyMatch(m -> Modifier.isPublic(m.getModifiers())
                        && Modifier.isStatic(m.getModifiers())
                        && m.getReturnType().getName().equals("java.lang.String")
                        && m.getName().equals("toStringue")
                        && Arrays.asList(m.getExceptionTypes()).isEmpty()
                );

        if (!hasPublicConformMethod)
            throw new Exception("Your service must have a public static 'toStringue' method without exceptions");

        return true;
    }
}
