package test_engine;

import test_engine.annotations.After;
import test_engine.annotations.Before;
import test_engine.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestsRunner {

    private TestsRunner() {
    }

    public static void runTests(Class<?> clazz) {

        final Method[] methods = clazz.getDeclaredMethods();
        // check annotations in test Class
        final TestCasesStore methodsStore = fillTestCasesStore(methods);
        final TestResultsStore testResultsStore = new TestResultsStore();

        // do test methods @Before(s) -> @Test -> @After(s) and store results
        while (methodsStore.hasNextTestCase()) {
            final var obj = createTestObj(clazz);
            final var testCase = methodsStore.nextTestCase();
            final var testResult = new TestResult(testCase.getTestMethod(), null);
            testResultsStore.addTestResult(testResult);
            try {
                for (Method beforeMethod : testCase.getBeforeMethods()) {
                    beforeMethod.invoke(obj);
                }
                testCase.getTestMethod().invoke(obj);
            } catch (InvocationTargetException | IllegalAccessException e) {
                testResult.setException(e.getCause());
            } finally {
                for (Method afterMethod : testCase.getAfterMethods()) {
                    try {
                        afterMethod.invoke(obj);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        if (testResult.getException() != null) {
                            testResult.getException().addSuppressed(e.getCause());
                        } else {
                            testResult.setException(e.getCause());
                        }
                    }
                }
            }
        }
        // Show tests result
        testResultsStore.showTestResults();
    }

    private static TestCasesStore fillTestCasesStore(Method[] methods) {
        TestCasesStore methodsStore = new TestCasesStore();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                methodsStore.addBeforeMethod(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                methodsStore.addTestMethod(method);
            } else if (method.isAnnotationPresent(After.class)) {
                methodsStore.addAfterMethod(method);
            }
        }
        return methodsStore;
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
