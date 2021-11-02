package test_engine;

import java.lang.reflect.Method;
import java.util.List;

public class TestCase {
    private final List<Method> beforeMethods;
    private final Method testMethod;
    private final List<Method> afterMethods;

    TestCase(List<Method> beforeMethods, Method testMethod, List<Method> afterMethods) {
        this.beforeMethods = beforeMethods;
        this.testMethod = testMethod;
        this.afterMethods = afterMethods;
    }

    List<Method> getBeforeMethods() {
        return beforeMethods;
    }

    Method getTestMethod() {
        return testMethod;
    }

    List<Method> getAfterMethods() {
        return afterMethods;
    }
}
