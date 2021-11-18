package ru.otus.dynamic_proxy;

import ru.otus.dynamic_proxy.annotations.Log;

public class TestLogging implements TestLoggingInterface {
    @Override
    @Log
    public void calculation(int param1) {
        System.out.println("This is a calculation method with 1 param int");
    }

    @Override
    public void calculation(int param1, int param2) {
        System.out.println("This is a calculation method with 2 param int");
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        System.out.println("This is a calculation method with 2 param int and 1 param String");
    }
}
