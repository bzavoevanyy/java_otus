package ru.otus.dynamic_proxy;

import java.lang.reflect.Proxy;

public class DemoProxy {
    public static void main(String[] args) {
        var myLoggingClass = (TestLoggingInterface) Proxy.newProxyInstance(DynamicInvocationHandler.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, new DynamicInvocationHandler(new TestLogging()));

        myLoggingClass.calculation(1);
        myLoggingClass.calculation(1, 2);
        myLoggingClass.calculation(1, 2, "123");

        var charSequence = (CharSequence) Proxy.newProxyInstance(DynamicInvocationHandler.class.getClassLoader(),
                new Class<?>[]{CharSequence.class}, new DynamicInvocationHandler("Hello world!"));

        System.out.println("String length:  " + charSequence.length());
    }
}
