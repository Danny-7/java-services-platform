package server.bri.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ValidateService {

    private ValidateService(){}

    public static boolean isConform(Class<?> service) throws Exception {
        Class<?>[] interfacesMetadata = service.getInterfaces();
        Constructor<?>[] constructorsMetadata = service.getConstructors();
        Field[] fields = service.getDeclaredFields();
        Method[] methods = service.getMethods();
        int modifiers = service.getModifiers();

        boolean hasImplementedService =  Arrays.asList(interfacesMetadata).contains(server.bri.Service.class);

        if(!hasImplementedService)
            throw new Exception("You have to implement the Service interface !");

        if(Modifier.isAbstract(modifiers) || !Modifier.isPublic(modifiers))
            throw new Exception("Your service has to be public and not abstract !");

        boolean hasEmptyConformConstructor = Arrays.stream(constructorsMetadata)
                .anyMatch(c -> c.getParameterCount() == 1 && Modifier.isPublic(c.getModifiers())
                        && Arrays.stream( c.getParameters()).anyMatch(p -> p.getType().getName().equals("java.net.Socket"))
                        && Arrays.asList(c.getExceptionTypes()).isEmpty()
                );

        if(!hasEmptyConformConstructor)
            throw new Exception("Your constructor has to be public with only one parameter(Socket) and not throws Exception !");

        Field socketField = Arrays.stream(fields)
                .filter(f -> f.getType().getName().equals("java.net.Socket")).findAny().get();
        int fieldModifiers = socketField.getModifiers();

        if(!Modifier.isFinal(fieldModifiers) || !Modifier.isPrivate(fieldModifiers))
            throw new Exception("Your socket field has to be final and private !");

        boolean hasPublicConformMethod = Arrays.stream(methods)
                .anyMatch(m -> Modifier.isPublic(m.getModifiers())
                    && Modifier.isStatic(m.getModifiers())
                    && m.getReturnType().getName().equals("java.lang.String")
                    && m.getName().equals("toStringue")
                    && Arrays.asList(m.getExceptionTypes()).isEmpty()
                );

        if(!hasPublicConformMethod)
            throw new Exception("Your service must have a public static 'toStringue' method without exceptions");

        return true;
    }
}
