package ru.otus.dynamic_proxy;

import ru.otus.dynamic_proxy.annotations.Log;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

public class DynamicInvocationHandler implements InvocationHandler {
    private final Object target;
    private final Map<String, Method> methods = new HashMap<>();

    DynamicInvocationHandler(Object target) {
        this.target = target;
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Log.class)) {
                methods.put(method.getName() + Arrays.toString(method.getParameters()), method);
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Method storedMethod = methods.get(method.getName() + Arrays.toString(method.getParameters()));

        if (storedMethod != null) {
            // Do method with logging
            System.out.println("----------------------------");
            System.out.println("Do method with logging: " + method.getName() + Arrays.toString(method.getParameters()));
            long start = System.nanoTime();
            Object result = storedMethod.invoke(target, args);
            System.out.println("Time elapsed: " + (System.nanoTime() - start));
            System.out.println("----------------------------");
            return result;
        } else {
            // Do method without logging
            return method.invoke(target, args);
        }
    }
}
