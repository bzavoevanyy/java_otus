package test_engine;

import test_engine.annotations.After;
import test_engine.annotations.Before;
import test_engine.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TestsRunner {

    private TestsRunner() {
    }

    public static boolean runTests(Class<?> clazz) throws Exception {

        boolean entireResult = true;
        final List<Method> beforeMethods = new ArrayList<>();
        final Map<Method, Throwable> testMethods = new HashMap<>();
        final List<Method> afterMethods = new ArrayList<>();

        final Method[] methods = clazz.getDeclaredMethods();

        // check annotations in test Class
        for (Method method : methods
        ) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.put(method, null);
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
        }

        // do test methods @Before(s) -> @Test -> @After(s)
        for (Map.Entry<Method, Throwable> testMethod : testMethods.entrySet()
        ) {
            final var obj = createTestObj(clazz);
            for (Method beforeMethod : beforeMethods
            ) {
                beforeMethod.invoke(obj);
            }

            try {
                testMethod.getKey().invoke(obj);
                System.out.println("\u001B[32mTest passed\u001B[0m");
            } catch (InvocationTargetException e) {
                System.out.println("\u001B[31mTest failed\u001B[0m");
                testMethods.put(testMethod.getKey(), e.getTargetException());
                entireResult = false;
            }

            for (Method afterMethod : afterMethods
            ) {
                afterMethod.invoke(obj);
            }
        }

        // show test results
        showTestResults(testMethods);

        return entireResult;
    }

    private static void showTestResults(Map<Method, Throwable> testMethods) {
        final var testPassed = Collections.frequency(testMethods.values(), null);
        final var testExecuted = testMethods.size();
        final var testFailed = testMethods.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("*****************************************************************");
        System.out.printf("%s test methods executed, %s passed,\u001B[31m %s failed%n\u001B[0m", testExecuted, testPassed, testFailed.size());
        testFailed.forEach((k, v) -> {
            System.out.println(k.getDeclaringClass().getName());
            System.out.println(k.getName() + " : " + "\u001B[31m" + v.getMessage() + "\u001B[0m");
            System.err.println(k.getName() + " " + v.getMessage());
            v.printStackTrace();
        });
    }

    private static Object createTestObj(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
