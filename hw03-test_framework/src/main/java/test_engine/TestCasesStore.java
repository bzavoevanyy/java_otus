package test_engine;

import java.lang.reflect.Method;
import java.util.*;

public class TestCasesStore {

    private final List<Method> beforeMethods = new ArrayList<>();
    private final Queue<Method> testMethods = new ArrayDeque<>();
    private final List<Method> afterMethods = new ArrayList<>();


    void addTestMethod(Method method) {
        this.testMethods.add(method);
    }

    void addBeforeMethod(Method method) {
        this.beforeMethods.add(method);
    }

    void addAfterMethod(Method method) {
        this.afterMethods.add(method);
    }

    TestCase nextTestCase() {
        TestCase testCase = null;
        if (!testMethods.isEmpty()) {
            testCase = new TestCase(beforeMethods, testMethods.poll(), afterMethods);
        }
        return testCase;
    }

    boolean hasNextTestCase() {
        return !testMethods.isEmpty();
    }

}
